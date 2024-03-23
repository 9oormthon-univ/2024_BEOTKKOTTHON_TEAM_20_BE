package com.beotkkot.qtudy.service.quiz;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import com.beotkkot.qtudy.domain.posts.Posts;
import com.beotkkot.qtudy.domain.quiz.Quiz;
import com.beotkkot.qtudy.domain.quiz.Review;
import com.beotkkot.qtudy.dto.object.QuizDto;
import com.beotkkot.qtudy.dto.object.QuizGradeListItem;
import com.beotkkot.qtudy.dto.object.QuizListItem;
import com.beotkkot.qtudy.dto.request.quiz.ChatMessageRequestDto;
import com.beotkkot.qtudy.dto.request.quiz.GenerateQuizRequestDto;
import com.beotkkot.qtudy.dto.request.quiz.GradeQuizRequestDto;
import com.beotkkot.qtudy.dto.request.quiz.PostQuizRequestDto;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import com.beotkkot.qtudy.dto.response.quiz.GetPostQuizResponseDto;
import com.beotkkot.qtudy.dto.response.quiz.QuizGradeResponseDto;
import com.beotkkot.qtudy.repository.posts.PostsRepository;
import com.beotkkot.qtudy.repository.quiz.QuizRepository;
import com.beotkkot.qtudy.repository.quiz.ReviewRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuizService {
    @Value("${GPT_API_KEY}")
    private String GPT_API_KEY;
    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private final QuizRepository quizRepo;
    private final ReviewRepository reviewRepo;
    private final PostsRepository postRepo;

    // 퀴즈 생성기
    public String generateQuiz(GenerateQuizRequestDto genQuizReqDto) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + GPT_API_KEY);

        List<ChatMessageRequestDto> messages = new ArrayList<>();

        messages.add(new ChatMessageRequestDto("system",
                "관련된 객관식 10문제 한국어로 출제. len(options)==4이며, answer은 단 하나만 존재하고 options의 index 반환. 출력 양식:\n" +
                        "[{\"question\": \"\",\n" +
                        "\"answer\": \"\",\n" +
                        "\"options\": [\"\", \"\", \"\", \"\"],\n" +
                        "\"explanation\": \"\"}]\""));
        messages.add(new ChatMessageRequestDto("user", genQuizReqDto.getSummary()));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        requestBody.put("model", "gpt-4-1106-preview");
        requestBody.put("temperature", 0.0f);
        requestBody.put("max_tokens", 4000);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(ENDPOINT, requestEntity, Map.class);
        Map<String, Object> responseBody = response.getBody();

        log.info("*** responseBody: " + responseBody);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        String content = (String) ((Map<String, Object>) firstChoice.get("message")).get("content");

        log.info("*** content: " + content);

        // content를 json으로 파싱 후 "data" list 안에 "question", "answer", "options",
        // "explanation"을 QuizDto에 담을 것
        ObjectMapper objectMapper = new ObjectMapper();
        List<QuizDto> quizDtoList = objectMapper.readValue(content, new TypeReference<List<QuizDto>>() {
        });

        log.info("*** quizDtoList: " + String.valueOf(quizDtoList));

        // data list length는 유동적
        // quizDto에 postId를 붙인 PostQuizRequestDto 생성
        for (QuizDto quizDto : quizDtoList) {
            PostQuizRequestDto saveQuizDto = new PostQuizRequestDto();
            saveQuizDto.setPostId(genQuizReqDto.getPostId());
            saveQuizDto.setTags(genQuizReqDto.getTags());
            saveQuizDto.setQuizDto(quizDto);
            saveQuiz(saveQuizDto);
        }
        return content;
    }

    @Transactional
    public void saveQuiz(PostQuizRequestDto saveQuizDto) {
        Quiz quiz = new Quiz();
        String optionsString = String.join(",", saveQuizDto.getQuizDto().getOptions());

        quiz.setPostId(saveQuizDto.getPostId());
        quiz.setTags(saveQuizDto.getTags());
        quiz.setQuestion(saveQuizDto.getQuizDto().getQuestion());
        quiz.setAnswer(saveQuizDto.getQuizDto().getAnswer());
        quiz.setOptions(optionsString);
        quiz.setExplanation(saveQuizDto.getQuizDto().getExplanation());

        quizRepo.save(quiz);
    }

    @Transactional
    public ResponseEntity<? super GetPostQuizResponseDto> getPostQuiz(Long postId) {
        List<QuizListItem> quizListItems = new ArrayList<>();
        List<String> answerList = new ArrayList<>();
        List<Long> quizIdList = new ArrayList<>();
        String type = "post";
        try {
            List<Quiz> quizzes = quizRepo.findAllByPostId(postId);
            for (Quiz quiz : quizzes) {
                answerList.add(quiz.getAnswer());
                quizIdList.add(quiz.getQuizId());
                quizListItems.add(QuizListItem.of(quiz));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        GetPostQuizResponseDto responseDto = new GetPostQuizResponseDto(quizListItems, answerList, quizIdList, type);
        return responseDto.success(quizListItems, answerList, quizIdList, type);
    }

    @Transactional
    public ResponseEntity<? super GetPostQuizResponseDto> getTagQuiz(String tagName) {
        List<QuizListItem> quizListItems = new ArrayList<>();
        List<String> answerList = new ArrayList<>();
        List<Long> quizIdList = new ArrayList<>();
        String type = "tag";
        try {
            List<Quiz> quizzes = quizRepo.findByTagName(tagName);
            for (Quiz quiz : quizzes) {
                answerList.add(quiz.getAnswer());
                quizIdList.add(quiz.getQuizId());
                quizListItems.add(QuizListItem.of(quiz));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        GetPostQuizResponseDto responseDto = new GetPostQuizResponseDto(quizListItems, answerList, quizIdList, type);
        return responseDto.success(quizListItems, answerList, quizIdList, type);
    }

    @Transactional
    public ResponseEntity<? super QuizGradeResponseDto> gradeQuiz(GradeQuizRequestDto dto, Long uuid) {
        List<QuizGradeListItem> gradeList = new ArrayList<>();
        List<String> answerList = new ArrayList<>(dto.getAnswerList());
        List<Integer> userAnswerList = new ArrayList<>(dto.getUserAnswerList());
        String reviewId = UUID.randomUUID().toString();

        Date now = Date.from(Instant.now());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String writeDatetime = simpleDateFormat.format(now);
        int totalScore = 0;

        try {
            for (int i = 0; i < answerList.size(); i++) {
                boolean correct = false;
                int score = 0; // 각 문제의 점수를 따로 계산하기 위해 반복마다 초기화
                Quiz quiz = quizRepo.findByQuizId(dto.getQuizIdList().get(i));
                Posts post = postRepo.findByPostId(quiz.getPostId());

                // 정답이 int로 입력되어있을 경우
                if (answerList.get(i).length() == 1) {
                    if (Integer.valueOf(answerList.get(i)) == userAnswerList.get(i)) {
                        score = 10;
                        correct = true;
                    }
                }
                // 정답이 options의 value로 있을 경우
                else {
                    List<String> options = Arrays.asList(quiz.getOptions().split("\\s*,\\s*"));
                    String userAnswer = options.get(userAnswerList.get(i));
                    if (answerList.get(i).equals(userAnswer)) {
                        score = 10;
                        correct = true;
                    }
                }

                // 오답노트 entity에 저장
                Review newReview = new Review();
                newReview.setUserId(uuid);
                newReview.setPostId(quiz.getPostId());
                newReview.setQuizId(quiz.getQuizId());
                newReview.setReviewId(reviewId);
                newReview.setType(dto.getType());
                newReview.setCreatedAt(writeDatetime);
                newReview.setUserAnswer(userAnswerList.get(i));
                newReview.setAnswer(answerList.get(i));
                newReview.setCorrect(correct);
                newReview.setExplanation(quiz.getExplanation());
                newReview.setCategoryId(post.getCategoryId());
                newReview.setScore(score);
                newReview.setTags(quiz.getTags());
                reviewRepo.save(newReview);

                gradeList.add(QuizGradeListItem.of(quiz, correct, userAnswerList.get(i)));
                totalScore += score; // 총점 누적
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        QuizGradeResponseDto responseDto = new QuizGradeResponseDto(gradeList, totalScore);
        return responseDto.success(gradeList, totalScore);
    }
}

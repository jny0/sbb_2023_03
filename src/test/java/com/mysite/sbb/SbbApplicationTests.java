package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private AnswerService answerService;


	@BeforeEach // 아래 메서드는 각 테스트케이스가 실행되기 전에 실행된다.
	void beforeEach() {
		// 모든 데이터 삭제
		answerRepository.deleteAll();
		answerRepository.clearAutoIncrement();
		// 모든 데이터 삭제
		questionRepository.deleteAll();
		// 흔적삭제(다음번 INSERT 때 id가 1번으로 설정되도록)
		questionRepository.clearAutoIncrement();

		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);  // 첫번째 질문 저장

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);  // 두번째 질문 저장

		// 답변 1개 생성
		Answer a1 = new Answer();
		a1.setContent("네 자동으로 생성됩니다.");
		//a1.setQuestion(q2); // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
		q2.addAnswer(a1);
		a1.setCreateDate(LocalDateTime.now());
		answerRepository.save(a1);

		//q2.getAnswerList().add(a1);
	}

	@Test
	@DisplayName("데이터 저장")
	void testJpa001() {
		// 질문 1개 생성
		Question q = new Question();
		q.setSubject("세계에서 가장 부유한 국가가 어디인가요?");
		q.setContent("알고 싶습니다.");
		q.setCreateDate(LocalDateTime.now());
		questionRepository.save(q);

		assertEquals("세계에서 가장 부유한 국가가 어디인가요?", questionRepository.findById(3).get().getSubject());
	}

	@Test
	@DisplayName("데이터 조회 findAll")
	void testJpa002() {
		List<Question> all = this.questionRepository.findAll();
		assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	@Test
	@DisplayName("데이터 조회 findById")
	void testJpa003() {
		Optional<Question> oq = this.questionRepository.findById(1);
		if(oq.isPresent()) {
			Question q = oq.get();
			assertEquals("sbb가 무엇인가요?", q.getSubject());
		}
	}

	@Test
	@DisplayName("데이터 조회 findBySubject")
	void testJpa004() {
		Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
		assertEquals(1, q.getId());
	}

	@Test
	@DisplayName("데이터 조회 findBySubjectAndContent")
	void testJpa005() {
		Question q = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		assertEquals(1, q.getId());
	}

	@Test
	@DisplayName("데이터 조회 Like **")
	void testJpa006() {
		List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
		Question q = qList.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	@Test
	@DisplayName("데이터 수정")
	void testJpa007() {
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent()); // isPresent() => 객체가 값을 가지고 있다면 true, 없다면 false
		Question q = oq.get();
		q.setSubject("수정된 제목");
		this.questionRepository.save(q);
	}

	@Test
	@DisplayName("데이터 삭제")
	void testJpa008() {
		assertEquals(2, this.questionRepository.count()); // 총 데이터의 수 확인

		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q);
		assertEquals(1, this.questionRepository.count()); // 삭제되었는지 확인. count=총 데이터의 수
	}

	@Test
	@DisplayName("답변 데이터 생성 후 저장 ")
	void testJpa009() {
		Question q = this.questionRepository.findById(2).orElse(null);

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q); // 어떤 질문의 답변인지 알기 위해서 Question 객체가 필요함
		a.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a);

	}

	@Test
	@DisplayName("답변 조회하기")
	void testJpa010(){
		Optional<Answer> oa = this.answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(2, a.getQuestion().getId());
	}
	@Transactional
	@Test
	@DisplayName("질문에 달린 답변 찾기")
	void testJpa011(){
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();

		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
	}

	@Test
	@DisplayName("테스트 데이터 300개 생성")
	void testJpa012() {
		for (int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			this.questionService.create(subject, content, null);
		}
	}

	@Test
	@DisplayName("답변 테스트 데이터 100개 생성")
	void testJpa013() {
		Question q = this.questionRepository.findById(2).orElse(null);
		Answer a = new Answer();

		for (int i = 1; i <= 100; i++) {
			String content = String.format("테스트 데이터입니다:[%03d]", i);
			this.answerService.create(q, content,null);
		}
	}


}

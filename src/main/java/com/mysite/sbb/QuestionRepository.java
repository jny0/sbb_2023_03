package com.mysite.sbb;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface QuestionRepository extends JpaRepository<Question, Integer> {
    //리포지터리의 대상이 되는 엔티티의 타입(Question)과 해당 엔티티의 PK의 속성 타입(Integer)을 지정
    Question findBySubject(String subject);
    // DI에 의해 스프링이 자동으로 QuestionRepository 객체를 생성한다. 이 때 프록시 패턴이 사용된다고 한다.
    // 즉 findBy + 엔티티의 속성명(예:findBySubject)과 같은 리포지터리 메서드를 작성하면 해당 속성의 값으로 데이터를 조회할수 있다.

    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);
}

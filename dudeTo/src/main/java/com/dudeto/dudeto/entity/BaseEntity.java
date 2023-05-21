package com.dudeto.dudeto.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


/*      @MappedSuperclass 상속관계 매핑이 아니다. @Embeddable로 사용할 수 있다 찾아보자
        엔티티가 아니고, 테이블과 매핑되지 않는다.
        부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공한다.
        직접 생성해서 사용할 일이 없으므로 추상 클래스로 사용하는 것이 좋다.
        테이블과 관계 없고 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할이다.
        주로 등록일, 수정일, 등록한 사람, 수정한 사람 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용한다.
        참고로 @Entity 클래스는 엔티티나 @MappedSuperclass로 지정한 클래스만 상속이 가능하다.*/

/*@EntityListeners는 Entity Listener는 엔티티의 변화를 감지하고 데이블의 데이터를 조작하는 일을 한다.
JPA에서는 아래의 7가지 Event를 제공한다.
@PrePersist : Persist(insert)메서드가 호출되기 전에 실행되는 메서드
@PreUpdate : merge메서드가 호출되기 전에 실행되는 메서드
@PreRemove : Delete메서드가 호출되기 전에 실행되는 메서드
@PostPersist : Persist(insert)메서드가 호출된 이후에 실행되는 메서드
@PostUpdate : merge메서드가 호출된 후에 실행되는 메서드
@PostRemove : Delete메서드가 호출된 후에 실행되는 메서드
@PostLoad : Select조회가 일어난 직후에 실행되는 메서드 */

// 결론은 모든 Entity가 공통으로 적용하는 정보일때 맵핑한다.
@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class BaseEntity {

    @CreationTimestamp // 생성된 시각
    @Column(updatable = false)//수정시 얘는 관여 안함.
    private LocalDateTime createTime;

    @UpdateTimestamp // 업데이트시 시각
    @Column(insertable = false)//삽입시 관여안함
    private LocalDateTime updateTime;
}





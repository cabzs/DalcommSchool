package dcsc.mvc.service.coupon;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import dcsc.mvc.domain.coupon.Coupon;
import dcsc.mvc.domain.coupon.CouponState;
import dcsc.mvc.domain.coupon.IssueCoupon;
import dcsc.mvc.domain.coupon.QCoupon;
import dcsc.mvc.domain.coupon.QIssueCoupon;
import dcsc.mvc.repository.coupon.CouponRepository;
import dcsc.mvc.repository.coupon.IssueCouponRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
	
	private final CouponRepository couponRep;
	private final IssueCouponRepository issueCouponRep;
	
	private final JPAQueryFactory factory;
	
	/**
	 * 자신이 보유한(발급 받은) 쿠폰 조회 기능; 학생
	 * @param studentId (검색 기준)
	 * @return List<IssueCoupon>
	 * */
	@Override
	public Page<IssueCoupon> selectByStudentId(String studentId, String couponUseable, Pageable pageable) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		
		QIssueCoupon issueCoupon = QIssueCoupon.issueCoupon;
		
		booleanBuilder.and(issueCoupon.student.studentId.eq(studentId));
		
		if(couponUseable.equals("useable")) {
			booleanBuilder.and(issueCoupon.issueUsable.eq("T"));
			booleanBuilder.and(issueCoupon.issueEndDate.after(LocalDateTime.now()));
		} else if(couponUseable.equals("used")) {
			booleanBuilder.and(issueCoupon.issueUsable.eq("F"));
		} else if(couponUseable.equals("expired")) {
			booleanBuilder.and(issueCoupon.issueUsable.eq("T"));
			booleanBuilder.and(issueCoupon.issueEndDate.before(LocalDateTime.now()));
		}
		
		JPQLQuery<IssueCoupon> jpqlQuery = factory.selectFrom(issueCoupon)
				.where(booleanBuilder)
				.orderBy(issueCoupon.issueEndDate.asc())
				.limit(pageable.getPageSize());
		
		Page<IssueCoupon> list = new PageImpl<IssueCoupon>(jpqlQuery.fetch(), pageable, jpqlQuery.fetchCount());

		return list;
	}
	
	/**
	 * 자신이 보유한(발급 받은) 쿠폰 조회 기능; 학생 -ajax
	 * */
	@Override
	public List<IssueCoupon> selectByStudentId(String studentId) {
		List<IssueCoupon> list = issueCouponRep.findByStudentStudentIdEquals(studentId);
		return list;
	}

	/**
	 * 클래스 발급 쿠폰 조회 기능 ; 선생님
	 * @param classId (검색 기준)
	 * @return List<Coupon>
	 * */
	@Override
	public List<Coupon> selectByClassId(Long classId) {
		List<Coupon> list = couponRep.findByClassesClassId(classId);
		return list;
	}
	
	

	/**
	 * 전체 발급 쿠폰 조회 기능 ; 선생님
	 * @param teacherId (검색 기준)
	 * @return List<Coupon>
	 * */
	@Override
	public List<Coupon> selectByTeacherId(String teacherId) {
		List<Coupon> list = couponRep.findByTeacherTeacherId(teacherId);
		return list;
	}
	
	/**
	 * 전체 발급 쿠폰 조회 기능 ; 선생님-페이징처리
	 * */
	@Override
	public Page<Coupon> selectByTeacherId(String teacherId, Pageable pageable) {
		
		return couponRep.findByTeacherTeacherId(teacherId, pageable);
	}
	
	
	/**
	 * 쿠폰 상세조회
	 * */
	@Override
	public Coupon selectByCouponId(Long couponId) {
		System.out.println("selectByCouponId의 couponId = "+ couponId);
		Coupon coupon = couponRep.findById(couponId).orElse(null);
		
		if(coupon==null) {
			throw new RuntimeException("발급쿠폰 상세보기에 오류가 발생습니다.");
		}
		return coupon;
	}

	/**
	 * 클래스 쿠폰 등록 기능 : 선생님
	 * 선생님ID와 클래스ID가 들어올 경우 해당 컬럼도 입력하고
	 * 들어오지 않을 경우 입력하지 않도록 동적 쿼리로 제작
	 * @param Coupon(teacherId, classId, couponName, couponDc, couponEndDate)
	 * @return void
	 * */
	@Override
	public void insertCoupon(Coupon coupon) {
		
		couponRep.save(coupon);	

	}

	/**
	 * 클래스 쿠폰 수정 기능: 선생님
	 * @param Coupon(couponId, couponName, couponDc, couponEndDate)
	 * @return void
	 * */
	@Override
	public void updateCoupon(Coupon coupon) {
		Coupon dbCoupon = couponRep.findById(coupon.getCouponId()).orElse(coupon);
		if(dbCoupon==null) {
			throw new RuntimeException("클래스 쿠폰이 수정되지 않았습니다.");
		}
		
		dbCoupon.setCouponName(coupon.getCouponName());
		dbCoupon.setCouponDc(coupon.getCouponDc());
		dbCoupon.setCouponEndDate(coupon.getCouponEndDate());
		
	}

	/**
	 * 클래스 쿠폰 삭제 기능 ; 선생님
	 * @param String couponId
	 * @return void
	 * */
	@Override
	public void deleteCoupon(Long couponId) {
		System.out.println("deleteCoupon의 couponId = "+ couponId);
		Coupon dbCoupon = couponRep.findById(couponId).orElse(null);
		
		if(dbCoupon==null) {
			throw new RuntimeException("쿠폰ID 오류로 삭제되지 않았습니다.");
		}
		
		couponRep.deleteById(couponId);

	}

	/**
	 * 클래스 쿠폰 상태 변경 기능 ; 선생님
	 * @param Coupon coupon(couponId,couponStateId)
	 * @return void
	 * */
	@Override
	public void updateCouponState(Coupon coupon) {
		Coupon dbCoupon = couponRep.findById(coupon.getCouponId()).orElse(null);
		
		if(dbCoupon==null) {
			throw new RuntimeException("쿠폰 상태를 변경하는 도중에 오류가 발생했습니다.");
		}
		
		List<Coupon> dbCouponList = couponRep.findByClassesClassId(dbCoupon.getClasses().getClassId());
		System.out.println(dbCouponList);
		for(Coupon c : dbCouponList) {
			c.setCouponState(new CouponState(2L, null));
		}
		
		dbCoupon.setCouponState(new CouponState(1L, null));
	}

	/**
	 * 등록한 전체 쿠폰(클래스 쿠폰 + 이벤트 쿠폰)조회 기능 ; 관리자
	 * @param 
	 * @return List<Coupon>
	 * */
	@Override
	public List<Coupon> selectAll() {
		List<Coupon> list = couponRep.findAll();
		return list;
	}
	
	/**
	 * 등록한 전체 쿠폰(클래스 쿠폰 + 이벤트 쿠폰)조회 기능 ; 관리자 - 페이징처리
	 * */
	@Override
	public Page<Coupon> selectAll(Pageable pageable) {
		
		return couponRep.findAll(pageable);
	}
	

	/**
	 * 학생이 보유한 쿠폰 조회 기능 ; 관리자(test)
	 * @param studentId(검색기준)
	 * @return List<IssueCoupon>
	 * */
	@Override
	public List<IssueCoupon> selectByStudent(String studentId) {
		List<IssueCoupon> list = issueCouponRep.findByStudentStudentIdEquals(studentId);
		return list;
	}
	

	/**
	 * 학생 아이디과 클래스 아이디로 발급받은 쿠폰 검색
	 * @param String studentId, Long classId
	 * @return List<IssueCoupon>
	 * */
	@Override
	public IssueCoupon selectIssueCouponByStudentIdAndClassId(String studentId, Long classId) {
		IssueCoupon coupon = issueCouponRep.findByStudentStudentIdEqualsAndCouponClassesClassId(studentId, classId);
		return coupon;
	}	
	
	/**
	 * 해당 클래스에 사용할 수 있는 쿠폰조회
	 * @param 
	 * @return List<Coupon>
	 * */
	/*@Override
	public List<Coupon> selectAllClassId(Long classId) {
		List<Coupon> list = couponRep.findByClassesClassId(classId);
		List<Coupon> nullList = couponRep.findByClassesClassIdIsNull(classId);
		
		List<Coupon> mergeList = new ArrayList<Coupon>();
		
		mergeList.addAll(list);
		mergeList.addAll(nullList);
		
		return mergeList;
	}*/

	
	/**
	 * 학생이 쿠폰 다운로드 하는 기능
	 * */
	@Override
	public void insertIssueCoupon(IssueCoupon issueCoupon) {
		Long couponId = issueCoupon.getCoupon().getCouponId();
		Coupon dbCoupon = couponRep.findById(couponId).orElse(null);
		
		Long classId = dbCoupon.getClasses().getClassId();
		String studentId = issueCoupon.getStudent().getStudentId();
		
		IssueCoupon coupon = selectIssueCouponByStudentIdAndClassId(studentId, classId);
		
		if(coupon != null) {
			throw new RuntimeException("이미 발급받은 쿠폰입니다");
		}
		
		Integer endDate = dbCoupon.getCouponEndDate(); //Integer
		
		LocalDateTime now = LocalDateTime.now();
		
		issueCoupon.setIssueStartDate(now);
		issueCoupon.setIssueEndDate(now.plusDays(endDate));
		issueCoupon.setIssueUsable("T");
		
		issueCouponRep.save(issueCoupon);
		
	}

	/**
	 * 클래스 쿠폰 상태 변경 기능 ; 관리자
	 * */
	@Override
	public void updateEventCouponState(Coupon coupon) {
		Coupon dbCoupon = couponRep.findById(coupon.getCouponId()).orElse(null);
		if(dbCoupon==null) {
			throw new RuntimeException("쿠폰 상태를 변경하는 도중에 오류가 발생했습니다.");
		}
		dbCoupon.setCouponState(coupon.getCouponState());
		
	}

	/**
	 * 클래스 아이디로 클래스 쿠폰 검색
	 * */
	@Override
	public Coupon selectByClassIdAndState(Long classId) {
		Coupon coupon = couponRep.findByClassesClassIdAndCouponStateCouponStateName(classId, "발급");
		
		return coupon;
	}
}

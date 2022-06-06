package dcsc.mvc.service.coupon;

import java.util.List;

import dcsc.mvc.domain.coupon.Coupon;
import dcsc.mvc.domain.coupon.IssueCoupon;

public interface CouponService {
	/**
	 * 자신이 보유한(발급 받은) 쿠폰 조회 기능; 학생
	 * @param studentId (검색 기준)
	 * @return List<IssueCoupon>
	 * */
	List<IssueCoupon> selectByStudentId(String studentId);
	
	/**
	 * 클래스 발급 쿠폰 조회 기능 ; 선생님
	 * @param classId (검색 기준)
	 * @return List<Coupon>
	 * */
	List<Coupon> selectByClassId(Long classId);
	
	
	/**
	 * 전체 발급 쿠폰 조회 기능 ; 선생님
	 * @param teacherId (검색 기준)
	 * @return List<Coupon>
	 * */
	List<Coupon> selectByTeacherId(String teacherId);
	
	
	/**
	 * 클래스 쿠폰 등록 기능
	 * 선생님ID와 클래스ID가 들어올 경우 해당 컬럼도 입력하고
	 * 들어오지 않을 경우 입력하지 않도록 동적 쿼리로 제작
	 * @param Coupon(teacherId, classId, couponName, couponDc, couponEndDate)
	 * @return void
	 * */
	void insertCoupon(Coupon coupon);
	
	
	/**
	 * 클래스 쿠폰 수정 기능
	 * @param Coupon(couponId, couponName, couponDc, couponEndDate)
	 * @return void
	 * */
	void updateCoupon(Coupon coupon);
	
	/**
	 * 클래스 쿠폰 삭제 기능 ; 선생님
	 * @param String couponId
	 * @return void
	 * */
	void deleteCoupon(String couponId);
	
	/**
	 * 클래스 쿠폰 상태 변경 기능 ; 선생님
	 * @param Coupon coupon(couponId,couponStateId)
	 * @return void
	 * */
	void updateCouponState(Coupon coupon);
	
	
	/**
	 * 등록한 전체 쿠폰(클래스 쿠폰 + 이벤트 쿠폰)조회 기능 ; 관리자
	 * @param 
	 * @return List<Coupon>
	 * */
	List<Coupon> selectAll();
	
	
	/**
	 * 학생이 보유한 쿠폰 조회 기능 ; 관리자
	 * @param studentId(검색기준)
	 * @return List<IssueCoupon>
	 * */
	List<IssueCoupon> selectByStudent(String studentId);

}

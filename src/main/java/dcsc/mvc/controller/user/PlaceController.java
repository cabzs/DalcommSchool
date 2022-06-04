package dcsc.mvc.controller.user;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dcsc.mvc.domain.user.Place;
import dcsc.mvc.domain.user.PlaceInfra;
import dcsc.mvc.domain.user.PlaceRegion;
import dcsc.mvc.domain.user.Teacher;
import dcsc.mvc.service.user.TeacherService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {
	
	public final TeacherService teacherService;
	
	/**
	 * 공방 등록 폼
	 * */
	@RequestMapping("/insertForm")
	public String insertForm() {
		return "teacher/teacherMypage/insertPlace";
	}
	
	/**
	 * 공방 등록하기
	 * */
	@RequestMapping("/insert")
	public String insertPlace(Place place, PlaceRegion placeRegion, PlaceInfra placeInfra) {
		
		place.setPlaceInfra(placeInfra);
		place.setPlaceRegion(placeRegion);
		teacherService.insertPlace(place);
		
		return "redirect:/teacher/teacherMypage/myPlace";
	}
	
	/**
	 * 공방 수정 폼
	 * */
	@RequestMapping("/updateForm")
	public ModelAndView updateForm(Long placeId) {
		Place place = teacherService.selectByPlaceId(placeId);
		
		return new ModelAndView("teacher/teacherMypage/updatePlace", "place", place);
	}
	
	/**
	 * 공방 수정하기
	 * */
	@RequestMapping("/update")
	public ModelAndView updatePlace(Place place) {
		teacherService.updatePlace(place);
		
		return new ModelAndView("teacher/teacherMypage/myPlace");
	}
	
	/**
	 * 공방 상세보기
	 * */
	@RequestMapping("/read")
	public ModelAndView readPlace(Long placeId) {
		Place place = teacherService.selectByPlaceId(placeId);
		return new ModelAndView("teacher/teacherMypage/placeDetail", "place", place);
	}
	
	/**
	 * 공방 인프라 리스트 가져오기
	 * */
	@RequestMapping("/selectPlaceInfra")
	public List<PlaceInfra> selectPlaceInfra(Long placeId) {
		List<PlaceInfra> list = teacherService.selectPlaceInfra(placeId);
		
		return list;
	}
	
	/**
	 * 공방 지역 가져오기
	 * */
	@RequestMapping("/selectPlaceRegion")
	@ResponseBody
	public List<PlaceRegion> selectPlaceRegion() {
		List<PlaceRegion> list = teacherService.selectPlaceRegion();
		
		return list;
	}
}

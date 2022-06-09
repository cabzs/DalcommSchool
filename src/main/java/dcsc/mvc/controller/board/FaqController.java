package dcsc.mvc.controller.board;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dcsc.mvc.domain.board.Faq;
import dcsc.mvc.domain.board.FaqCategory;
import dcsc.mvc.service.board.FaqService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/board/FAQ")
@RequiredArgsConstructor
public class FaqController {
	
	private final FaqService faqService;
	
	/**
	 * 
	 * */
	@RequestMapping("/faqList")
	private String faqList(Model model) {
		List<Faq> faqlist = faqService.selectAllfqa();
		
		model.addAttribute("faqlist",faqlist);
		
		return "admin/board/FAQ/faqList";
	}
	
	@RequestMapping("/faqCategoryList")
	private String faqCategoryList(Model model,Long faqCategoryId) {
		
		System.out.println(faqCategoryId);
		List<Faq> faqlist=faqService.selectByfaqCategoryId(faqCategoryId);
		model.addAttribute("faqlist",faqlist);
		return "redirect:/admin/board/FAQ/faqList";
	}
	
	/**
	 * 글 등록폼
	 * */
	@RequestMapping("/write")
	public String write() {
		
		return "admin/board/FAQ/faqWrite";
	}
	
	/**
	 * 등록하기
	 * */
	@RequestMapping("/faqWrite")
	public String insertFAQ(Faq faq, FaqCategory faqCategory) {
		faq.setFaqCategory(faqCategory);
		faqService.insertFAQ(faq);
		
		return "redirect:/admin/board/FAQ/faqList";
	}
	
	/**
	 * 상세보기
	 * */
	@RequestMapping("/faqRead/{faqNo}")
	public ModelAndView read(@PathVariable Long faqNo,String flag) {
		boolean state = flag==null ? true : false;
		
		Faq faq = faqService.selectByFaqNo(faqNo, state);//true는 조회수 증가!!
		
		return new ModelAndView("admin/board/FAQ/faqRead","faq",faq);
	}
	
	/**
	 * 수정폼
	 * */
	@RequestMapping("/updateForm")
	public ModelAndView updateForm(Long faqNo,boolean state) {
		Faq faq = faqService.selectByFaqNo(faqNo,false);
		
		return new ModelAndView("admin/board/FAQ/faqUpdate","faq",faq);
	}
	
	/**
	 * 수정하기
	 * */
	@RequestMapping("/faqUpdate")
	public String updateFAQ(Faq faq,FaqCategory faqCategory) {
		faq.setFaqCategory(faqCategory);
		faqService.updateFAQ(faq, faqCategory);
	System.out.println("faqCategory" + faqCategory);
		
		return "redirect:/admin/board/FAQ/faqRead/" + faq.getFaqNo();
	}
	
	/**
	 * 삭제하기
	 * */
	@RequestMapping("/deleteFAQ")
	public String deleteFAQ(Long faqNo) {
		faqService.deleteFAQ(faqNo);
		
		return "redirect:/admin/board/FAQ/faqList";
	}
	
	/**
	 * 카테고리 조회
	 * */
	@RequestMapping("/faqCategory")
	@ResponseBody
	public List<FaqCategory> selectfaqCategory() {
		List<FaqCategory> list = faqService.selectfaqCategory();
		
		return list;
	}
	
	/**
	 * 카테고리 별로 정렬
	 * */
	@RequestMapping("/faqCategoryId")
	public List<Faq> selectByfaqCategoryId(){
		List<Faq> list = faqService.selectByfaqCategoryId(null);
		
		return list;
	}
	/**
	 * 검색 하기
	 * */
	@RequestMapping("/faqSearch")
	public ModelAndView selectByKeyword(String keyword) {
		System.out.println("keyfield : " +" / " + "keyword : "+ keyword);
		List<Faq> list = faqService.selectBykeyword(keyword);
		
		return new ModelAndView("/admin/board/FAQ/faqList","faqlist",list);
		
	}
	
	
}
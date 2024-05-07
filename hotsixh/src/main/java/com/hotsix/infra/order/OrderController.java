package com.hotsix.infra.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hotsix.common.constants.Constants;
import com.hotsix.common.util.UtilDateTime;
import com.hotsix.infra.code.CodeVo;
import com.hotsix.infra.member.MemberService;
import com.hotsix.infra.member.MemberVo;
import com.hotsix.infra.placingorder.PlacingOrderDto;
import com.hotsix.infra.product.ProductService;
import com.hotsix.infra.product.ProductVo;

@Controller
public class OrderController {

	@Autowired
	OrderService service;
	
	@Autowired
	MemberService mservice;
	
	@Autowired
	ProductService pservice;
	
	public void setSearch(OrderVo vo) throws Exception {
		/* 최초 화면 로딩시에 세팅은 문제가 없지만 */
		/*이후 전체적으로 데이터를 조회를 하려면 null 값이 넘어 오는 관계로 문제가 전체 데이터 조회가 되지 못한다.*/
		/*해서 BaseVo.java 에서 기본값을 주어서 처리*/
//		vo.setShUseNy(vo.getShUseNy() == null ? 1 : vo.getShUseNy());
//		vo.setShDelNy(vo.getShDelNy() == null ? 0 : vo.getShDelNy());
//		vo.setShOptionDate(vo.getShOptionDate() == null ? 2 : vo.getShOptionDate());
		
		/* 초기값 세팅이 있는 경우 사용 */
		vo.setShDateStart(vo.getShDateStart() == null
		    ? UtilDateTime.calculateDayReplace00TimeString(UtilDateTime.nowLocalDateTime(), Constants.DATE_INTERVAL)
		    : UtilDateTime.add00TimeString(vo.getShDateStart()));
		vo.setShDateEnd(vo.getShDateEnd() == null
		    ? UtilDateTime.nowString()
		    : UtilDateTime.add59TimeString(vo.getShDateEnd()));		
		
//		/* 초기값 세팅이 없는 경우 사용 */
//		vo.setShDateStart(vo.getShDateStart() == null || vo.getShDateStart() == "" ? null : UtilDateTime.add00TimeString(vo.getShDateStart()));
//		vo.setShDateEnd(vo.getShDateEnd() == null || vo.getShDateEnd() == "" ? null : UtilDateTime.add59TimeString(vo.getShDateEnd()));
		
		
	}
	
	
	
	
	@RequestMapping(value = "/orderXdmList")
	public String orderXdmList(@ModelAttribute("vo") OrderVo vo, Model model) throws Exception {
		
		setSearch(vo);
		
		model.addAttribute("count", service.selectOneCount(vo));
		
		
		vo.setParamsPaging(service.selectOneCount(vo));
		
		if (vo.getTotalRows() > 0) {
			model.addAttribute("list", service.selectList(vo));
		}
		
		return "adm/infra/order/orderList";
	}
	
	@RequestMapping(value = "/orderAdd")
	public String orderAdd(@ModelAttribute("vo") MemberVo mvo,ProductVo pvo, Model model) throws Exception {
		
		model.addAttribute("list", mservice.memberList(mvo));
		model.addAttribute("plist", pservice.productList(pvo));
		
		return "adm/infra/order/orderAdd";
	}
	
	@RequestMapping(value = "/orderForm")
	public String orderForm(@ModelAttribute("vo") ProductVo pvo,OrderDto dto, Model model) throws Exception {
		
		model.addAttribute("item", service.selectOne(dto));
		model.addAttribute("plist", pservice.selectList(pvo));
		
		return "adm/infra/order/orderForm";
	}
	
	@RequestMapping(value = "/orderInsert")
	public String orderInsert(OrderDto dto) throws Exception{
		
		service.insert(dto);
		return "redirect:/orderXdmList";
	}
	
	@RequestMapping(value="/orderUpdate")
	public String orderUpdate(OrderDto dto) throws Exception {
		
		service.update(dto);
	
		return "redirect:/orderXdmList";
	}
	
	@RequestMapping(value="/orderDelete")
	public String orderDelete(OrderDto dto) throws Exception {
		
		service.delete(dto);
	
		return "redirect:/orderXdmList";
	}
	
	@RequestMapping(value="/orderUelete")
	public String orderUelete(OrderDto dto) throws Exception {
		
		service.uelete(dto);
	
		return "redirect:/orderXdmList";
	}
}

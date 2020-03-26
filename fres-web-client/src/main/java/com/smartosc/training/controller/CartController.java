package com.smartosc.training.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smartosc.training.config.ServicesConfig;
import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.CartLineDTO;
import com.smartosc.training.dto.OrderDTO;
import com.smartosc.training.dto.OrderDetailDTO;
import com.smartosc.training.dto.ProductDTO;
import com.smartosc.training.dto.UserDTO;
import com.smartosc.training.service.RestTemplateService;
import com.smartosc.training.utils.SecurityUtil;

@Controller
@RequestMapping(value = "/cart")

public class CartController {

	@Autowired
	RestTemplateService RestTemplate;

	@Autowired
	ServicesConfig servicesConfig;

	@GetMapping("/Cart-Info")
	public String index(ModelMap modelMap, HttpSession session) {
		if (session.getAttribute("cart") != null) {
			@SuppressWarnings("unchecked")
			List<CartLineDTO> listCart = (List<CartLineDTO>) session.getAttribute("cart");
			modelMap.addAttribute("cart", listCart);
			modelMap.put("total", total(session));

		}
		return "cart/Cart-Info";

	}

	@RequestMapping(value = "/buy/{id}", method = RequestMethod.GET)
	public String buy(@PathVariable("id") Long id, ModelMap modelMap, HttpSession session,
			HttpServletRequest httpServletRequest) {
		String url = servicesConfig.getHotsName().concat(servicesConfig.getProductAPI()) + "/" + id;
		ParameterizedTypeReference<APIResponse<ProductDTO>> reponseType = new ParameterizedTypeReference<APIResponse<ProductDTO>>() {
		};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		ProductDTO productDTO = RestTemplate.getSomething(url, HttpMethod.GET, headers, null, reponseType);
		if (session.getAttribute("cart") == null) {
			List<CartLineDTO> cart = new ArrayList<CartLineDTO>();
			cart.add(new CartLineDTO(productDTO, 1));
			session.setAttribute("cart", cart);
		} else {
			@SuppressWarnings("unchecked")
			List<CartLineDTO> listCart = (List<CartLineDTO>) session.getAttribute("cart");
			int index = isExits(id, listCart);
			if (index == -1) {
				listCart.add(new CartLineDTO(productDTO, 1));
			} else {

				int quantity = listCart.get(index).getQuantity() + 1;
				listCart.get(index).setQuantity(quantity);
			}
			session.setAttribute("cart", listCart);
		}
		return "redirect:/cart/Cart-Info";
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public String myCart(HttpSession session,ModelMap modelMap) {
		if (session.getAttribute("cart")!=null) {
		List<CartLineDTO> listCart = (List<CartLineDTO>) session.getAttribute("cart");
		modelMap.addAttribute("cart",listCart);
		}
		
		return "";
	}
	
	private int isExits(Long id,List<CartLineDTO> list) {
		 for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getProductDTO().getId() == id) {
				return i;
			}
		}
		return -1;
	}

	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public String remove(@PathVariable("id") Long id, HttpSession session) {
		@SuppressWarnings("unchecked")
		List<CartLineDTO> listCart = (List<CartLineDTO>) session.getAttribute("cart");
		int index = isExits(id, listCart);
		listCart.remove(index);
		session.setAttribute("cart", listCart);

		return "redirect:/cart/Cart-Info";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(HttpServletRequest request, HttpSession session) {
		String[] quantites = request.getParameterValues("quantity");
		@SuppressWarnings("unchecked")
		List<CartLineDTO> listCart = (List<CartLineDTO>) session.getAttribute("cart");
		for (int i = 0; i < listCart.size(); i++) {
			listCart.get(i).setQuantity(Integer.parseInt(quantites[i]));
		}

		return "redirect:/cart/Cart-Info";
	}

	public double total(HttpSession session) {
		@SuppressWarnings("unchecked")
		List<CartLineDTO> listCart = (List<CartLineDTO>) session.getAttribute("cart");
		double total = 0;
		for (CartLineDTO cartLineDTO : listCart) {
			total += cartLineDTO.getQuantity() * Double.valueOf(cartLineDTO.getProductDTO().getPrice());
		}
		return total;
	}

	@PostMapping(value = "/order")
	public String checkout(HttpSession session, @ModelAttribute("order") OrderDTO orderDTO,HttpServletRequest request) {

		String url = servicesConfig.getHotsName().concat(servicesConfig.getOrderAPI());
		ParameterizedTypeReference<APIResponse<OrderDTO>> reponseType = new ParameterizedTypeReference<APIResponse<OrderDTO>>() {
		};
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		
		orderDTO.setName("default order name");
		orderDTO.setUserDTOs(SecurityUtil.getPrincipal().getUser());
		orderDTO.setTotalPrice(total(session));
		
		@SuppressWarnings("unchecked")
		List<CartLineDTO> listCart = (List<CartLineDTO>) session.getAttribute("cart");
		
		List<OrderDetailDTO> detailDTO = new ArrayList<>();
		
        listCart.forEach(d->{
        	OrderDetailDTO dto = new OrderDetailDTO();
        	dto.setProduct(d.getProductDTO());
        	dto.setQuantity(d.getQuantity());
        	
        	double a = d.getQuantity() * Double.valueOf(d.getProductDTO().getPrice());
        	dto.setPrice(String.valueOf(a));
        	
        	
        	detailDTO.add(dto);
        });
        
        orderDTO.setDetails(detailDTO);
		RestTemplate.getSomething(url, HttpMethod.POST, headers, orderDTO, reponseType);
		request.getSession(true).removeAttribute("cart");
		return "products/thankyou";
	}
	
	@GetMapping(value = "/checkout")
	public String checkoutOrder(ModelMap model,@ModelAttribute("order")OrderDTO dto,HttpSession session) {
		UserDTO dto2 = SecurityUtil.getPrincipal().getUser();
		model.addAttribute("user", dto2);
		@SuppressWarnings("unchecked")
		List<CartLineDTO> listCart = (List<CartLineDTO>) session.getAttribute("cart");
		session.setAttribute("cart", listCart);
		model.put("total", total(session));
		return "cart/CheckOut";
	}
	

	

}

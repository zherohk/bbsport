package com.zhero.babasport.controller.cart;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhero.babasport.pojo.cart.BuyerCart;
import com.zhero.babasport.pojo.cart.BuyerItem;
import com.zhero.babasport.pojo.product.Sku;
import com.zhero.babasport.service.buyer.BuyerService;
import com.zhero.babasport.service.sessionprovider.SessionProvider;
import com.zhero.babasport.utils.constans.BbsConstans;
import com.zhero.babasport.utils.token.TokenUtils;

/**
 * @description 购物车模块
 * @author zhero
 * @date 2017年11月22日
 */
@Controller
public class CartController {

	@Resource
	private BuyerService buyerService;
	
	@Resource
	private SessionProvider sessionProvider;
	
	/**
	 * 加入购物车操作
	 * @param skuId
	 * @param amount
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/shopping/buyerCart")
	public String buyerCart(Long skuId, Integer amount, 
			HttpServletRequest req, HttpServletResponse resp, Model model) throws Exception {
		//定义一个空车
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		BuyerCart buyerCart = getBuyerCart(req, om);
		//没有,第一次将 商品加入购物车,创建新车.
		if (null == buyerCart) {
			buyerCart = new BuyerCart();
		}
		//创建购物项,填充数据(瘦身的)
		BuyerItem buyerItem = new BuyerItem();
		buyerItem.setAmount(amount);
		Sku sku = new Sku();
		sku.setId(skuId);
		buyerItem.setSku(sku);
		//将购物项装车
		buyerCart.addBuyerItem(buyerItem);
		
		String username = sessionProvider.getAttribute(TokenUtils.getCSESSIONID(req, resp));
		if (null != username) {//已登录,保存到Redis中
			if (null != buyerCart) {
				buyerService.insertBuyerCartToRedis(username, buyerCart);
				//清空buyerCartCookie
				clearBuyerCartCookie(resp);
			}
		} else {//未登录保存到cookie中
			//将第一次创建的购物车,保存到cookie中
			StringWriter w = new StringWriter();
			om.writeValue(w, buyerCart);//对象转成json串
			Cookie cookie = new Cookie(BbsConstans.COOKIE_BUYER_CART, w.toString());
			cookie.setMaxAge(60*60);
			cookie.setPath("/");
			resp.addCookie(cookie);
			//重定向到查询的方法
		}
		
		return "redirect:/shopping/toCart";
	}
	
	/**
	 * 查询购物车中商品信息
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws Exception
	 */
	@RequestMapping("/shopping/toCart")
	public String toCart(Model model, HttpServletRequest req, HttpServletResponse resp) throws JsonParseException, JsonMappingException, Exception {
		//未登录,从cookie中查
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		BuyerCart buyerCart = getBuyerCart(req, om);
		//已登录,从Redis查
		String username = sessionProvider.getAttribute(TokenUtils.getCSESSIONID(req, resp));
		if (null != username) {
			//将本地cookie同步到Redis
			if (null != buyerCart) {
				List<BuyerItem> items = buyerCart.getBuyerItems();
				if (null != items && items.size() > 0) {
					buyerService.insertBuyerCartToRedis(username, buyerCart);
					//清空buyerCartCookie
					clearBuyerCartCookie(resp);
				}
			}
			buyerCart = buyerService.getBuyerCartFromRedis(username);
		}
		//sku瘦身后,jsp页面回显数据,需要回填sku的数据
		if (null != buyerCart) {
			List<BuyerItem> buyerItems = buyerCart.getBuyerItems();
			if (null != buyerItems && buyerItems.size() > 0) {
				for (BuyerItem item : buyerItems) {
					item.setSku(buyerService.selectSkuById(item.getSku().getId()));
				}
			}
			model.addAttribute("buyerCart", buyerCart);
		}
		return "cart";
	}

	/**
	 * 
	 * 清空本地cookie信息
	 * @param resp
	 */
	private void clearBuyerCartCookie(HttpServletResponse resp) {
		//清空本地cookie信息
		Cookie cookie = new Cookie(BbsConstans.COOKIE_BUYER_CART, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		resp.addCookie(cookie);
	}

	/**
	 * 从cookie中查询购物车信息
	 * @param req
	 * @param om
	 * @return
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	private BuyerCart getBuyerCart(HttpServletRequest req, ObjectMapper om)
			throws IOException, JsonParseException, JsonMappingException {
		BuyerCart buyerCart = null;
		//判断本地是否有购物车
		Cookie[] cookies = req.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				//如果有,在原有的购物车中继续追加购物项
				if (BbsConstans.COOKIE_BUYER_CART.equals(cookie.getName())) {
					//购物车的json串
					String content = cookie.getValue();
					buyerCart = om.readValue(content, BuyerCart.class);//json串转成对象
					break;
				}
			}
		}
		return buyerCart;
	}
}

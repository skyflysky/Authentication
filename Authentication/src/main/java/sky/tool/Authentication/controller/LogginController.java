package sky.tool.Authentication.controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sky.tool.Authentication.common.HttpResultConstant;
import sky.tool.Authentication.common.JsonResult;
import sky.tool.Authentication.dto.AbsLogginDto;
import sky.tool.Authentication.entity.abstractt.AbstractLoggin;
import sky.tool.Authentication.entity.abstractt.AbstractUser;
import sky.tool.Authentication.service.LogginService;
import sky.tool.Authentication.service.UserService;

@RestController
@RequestMapping("/loggin")
public class LogginController
{
	@Resource(name = "authUser")
	UserService userService;
	
	@Resource(name = "defaultAuthLoggin")
	LogginService logginService;
	
	@PostMapping("/generate")
	@Transactional
	public JsonResult generate(@RequestParam(required=true) Integer uid)
	{
		AbstractUser absUser = userService.getOne(uid);
		if(absUser == null)
		{
			return JsonResult.build(HttpResultConstant.UNREACHABLE_ENTITY);
		}
		AbstractLoggin loggin = logginService.generateLoggin(absUser);
		AbsLogginDto dto = AbsLogginDto.fromEntity(loggin);
		return JsonResult.success(dto);
	}
	
	@PostMapping("/token")
	public JsonResult token(@RequestParam(required=true) String account , @RequestParam(required=true) String password)
	{
		AbstractLoggin loggin = logginService.authLoggin(account, password);
		if(loggin != null)
		{
			return JsonResult.success(loggin.getToken());
		}
		return JsonResult.build(HttpResultConstant.VERIFICATION_FAIL);
	}
	
	
}

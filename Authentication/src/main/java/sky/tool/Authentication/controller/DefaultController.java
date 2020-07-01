package sky.tool.Authentication.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sky.tool.Authentication.common.JsonResult;

@RestController
@RequestMapping("/default")
public class DefaultController
{
	@PostMapping("/default")
	public JsonResult ss()
	{
		return JsonResult.success();
	}
}

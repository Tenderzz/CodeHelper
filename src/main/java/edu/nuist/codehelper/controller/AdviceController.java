package edu.nuist.codehelper.controller;

import edu.nuist.codehelper.common.R;
import edu.nuist.codehelper.entity.Advice;
import edu.nuist.codehelper.entity.AdviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class AdviceController {
    @Autowired
    private AdviceService adviceService;

    @RequestMapping("/adviceacc")
    public R<String> advicesave(Advice advice) {
        System.out.println(advice);
        adviceService.save(advice);
        return R.success("提议成功~");
    }
}

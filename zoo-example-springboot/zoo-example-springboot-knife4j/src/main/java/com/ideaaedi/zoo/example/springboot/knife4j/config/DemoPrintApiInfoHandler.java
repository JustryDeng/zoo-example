package com.ideaaedi.zoo.example.springboot.knife4j.config;

import com.ideaaedi.zoo.diy.artifact.apidoc.knife4j.apiinfo.AbstractApiInfoHandler;
import com.ideaaedi.zoo.diy.artifact.apidoc.knife4j.entity.DefaultApiDetailDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * (non-javadoc)
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Component
public class DemoPrintApiInfoHandler extends AbstractApiInfoHandler {

    @Override
    public void apiDetailList(@Nonnull List<DefaultApiDetailDTO> apiDetailInfoList) {
        int size = apiDetailInfoList.size();
        System.err.println("-------------- 当前项目下，有这些api --------------");
        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException ignore) {
        }
        for (int i = 1; i <= size; i++) {
            DefaultApiDetailDTO defaultApiDetail = apiDetailInfoList.get(i - 1);
            System.err.printf("%s. api名称：%s，\t请求方法：%s，\tapi地址：%s\n",
                    i,
                    StringUtils.defaultIfBlank(defaultApiDetail.getClassDesc(), "") + "-" + StringUtils.defaultIfBlank(defaultApiDetail.getMethodDesc(), ""),
                    String.join("或", defaultApiDetail.getRequestPathSet()),
                    defaultApiDetail.getRequestMethodSet().stream().map(RequestMethod::name).collect(Collectors.joining("、"))
                    );
        }
    }
}

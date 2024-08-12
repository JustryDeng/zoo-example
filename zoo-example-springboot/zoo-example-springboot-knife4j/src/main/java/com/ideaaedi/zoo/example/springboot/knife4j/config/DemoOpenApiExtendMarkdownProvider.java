package com.ideaaedi.zoo.example.springboot.knife4j.config;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendMarkdownChildren;
import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendMarkdownFile;
import com.google.common.collect.Lists;
import com.ideaaedi.zoo.diy.artifact.apidoc.knife4j.markdown.OpenApiExtendMarkdownProvider;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class DemoOpenApiExtendMarkdownProvider implements OpenApiExtendMarkdownProvider {
    @Override
    public List<OpenApiExtendMarkdownFile> provideMarkdowns() {
        // 自定义文档菜单1
        OpenApiExtendMarkdownFile openApiExtendMarkdownFile1 = new OpenApiExtendMarkdownFile();
        openApiExtendMarkdownFile1.setGroup("A组"); // 填${springdoc.group-configs[x].display-name}对应值，表示将本菜单展示在哪个分组下
        openApiExtendMarkdownFile1.setName("动态markdown文档菜单XYZ"); // 菜单名（一级菜单）
        // 几个children则表示几个markdown内容
        OpenApiExtendMarkdownChildren children1 = new OpenApiExtendMarkdownChildren();
        children1.setTitle("熬夜好不好");  // markdown标题（二级菜单）
        // markdown内容
        children1.setContent("""
                # 还熬夜？
                1. 冷了要穿衣服？
                  ```java
                  // 没有为什么
                  ```
                2. 困了要睡觉
                """);
        
        openApiExtendMarkdownFile1.setChildren(Lists.newArrayList(children1));
        
        // 自定义文档菜单2
        OpenApiExtendMarkdownFile openApiExtendMarkdownFile2 = new OpenApiExtendMarkdownFile();
        openApiExtendMarkdownFile2.setGroup("B组");
        openApiExtendMarkdownFile2.setName("动态markdown文档菜单QWER");
        OpenApiExtendMarkdownChildren children2 = new OpenApiExtendMarkdownChildren();
        children2.setTitle("不要憋尿");
        children2.setContent("""
                # 憋尿不好
                *再过10分钟睡觉啦！*，`嘿嘿`！
                """);
        openApiExtendMarkdownFile2.setChildren(Lists.newArrayList(children2));
        
        return Lists.newArrayList(
                openApiExtendMarkdownFile1,
                openApiExtendMarkdownFile2
        );
    }
}

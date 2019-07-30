package com.miaoshaproject.controller;

import com.miaoshaproject.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author cyx
 * @data 2019/4/1 14:50
 */
@Controller
//这个类增加一个模块路径
@RequestMapping("/student")
public class StudentController {
    List<Student> students=new ArrayList<Student>();
    //增加一个默认的映射
    @RequestMapping("/")
    public String showStudent(Model model){
        Student s1=new Student(1L,"20000000","bob",
                "smith","male",new Date());
        model.addAttribute("student",s1);
        model.addAttribute("students",students);
        return "student/index";
    }
    public StudentController(){
        Student s1=new Student(1L,"20000000","bob",
                "smith","male",new Date());
        Student s2=new Student(2L,"20000000","小明",
                "ming","male",new Date());
        Student s3=new Student(3L,"20000000","小强",
                "qiang","male",new Date());
        students.add(s1);
        students.add(s2);
        students.add(s3);
    }

    //显示form的处理函数
    @GetMapping("form")
    public String showFormPage(){
        return "student/form";
    }
    //添加学生的handler
    @RequestMapping("/add")
    public String addStudent(Student student){
        students.add(student);
        return "redirect:/student/";
    }

    //删除学生的handler
    @RequestMapping("/del")
    public String delStudent(Long id){
        for(Student s:students){
            if(s.getId().equals(id)){
                students.remove(s);
            }
        }
        return "redirect:/student/";
    }

    @RequestMapping("/edit")
    public String editStudent(Long id,Student student){
        for(Student s:students){
            if(s.getId().equals(id)){
               students.remove(s);
               students.add(student);
            }
        }
        return "redirect:/student/";
    }

    @RequestMapping("/editform/{id}")
    public String editStudent(Model model,@PathVariable("id") Long id){
        for(Student s:students){
            if(s.getId().equals(id)){
                model.addAttribute("s",s);
                break;
            }
        }
        return "student/editform";
    }
}

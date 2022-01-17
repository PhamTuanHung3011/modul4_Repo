package codegym.controller;

import codegym.model.ClassRoom;
import codegym.model.Student;
import codegym.service.IClassZoomService;
import codegym.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
public class StudentController {
    @Autowired
    IStudentService studentService;

    @Autowired
    IClassZoomService classZoomService;

    @GetMapping("/students")
    public ModelAndView showAll(){
        ModelAndView modelAndView = new ModelAndView("show");
        modelAndView.addObject("students", studentService.findAll());
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreate(){
        ModelAndView modelAndView = new ModelAndView("create");
        modelAndView.addObject("student", new Student());
        modelAndView.addObject("classZooms", classZoomService.findAll());
        return modelAndView;
    }

    @PostMapping("/create")
    public String create(@ModelAttribute(value = "student") Student student, @RequestParam long idClassZoom, @RequestParam MultipartFile upImg){
        ClassRoom classRoom = new ClassRoom();
        classRoom.setId(idClassZoom);
        student.setClassRoom(classRoom);

        String nameFile = upImg.getOriginalFilename();
        try {
            FileCopyUtils.copy(upImg.getBytes(), new File("E:\\Tu\\modul4\\Demo_Repository_JPA-master\\Demo_Repository_JPA-master\\src\\main\\webapp\\WEB-INF\\img\\" + nameFile));
            student.setImg("/img/"+nameFile);
            studentService.save(student);

        } catch (IOException e) {
            student.setImg("/img/abc.jpeg");
            studentService.save(student);
            e.printStackTrace();
        }
        return "redirect:/students";
    }

    @GetMapping("/delete")
    public String deleteStudent(@RequestParam long id) {
       Student student1 = studentService.findById(id);
       String fileImg = student1.getImg().replaceAll("/img/", "");
       String file1 = "E:\\Tu\\modul4\\Demo_Repository_JPA-master\\Demo_Repository_JPA-master\\src\\main\\webapp\\WEB-INF\\img\\" + fileImg;
        System.out.println(file1);
        File file = new File(file1);
        file.delete();
        studentService.delete(id);
        return "redirect:/students";
    }

    @GetMapping("/edit")
    public ModelAndView showEdit(@RequestParam long id) {
        ModelAndView modelAndView = new ModelAndView("/edit");
        modelAndView.addObject("student", studentService.findById(id));
        modelAndView.addObject("classZooms", classZoomService.findAll());
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView edit(@ModelAttribute Student student, @RequestParam MultipartFile upImg, @RequestParam long idClassZoom) {
        ClassRoom classRoom = new ClassRoom();
        classRoom.setId(idClassZoom);
        student.setClassRoom(classRoom);


        if (upImg.getSize() !=0) {
            String fileName = upImg.getOriginalFilename();
//            String nameDelete = student.getImg().replaceAll("/img/", "");
            String nameDelete1 = "E:\\Tu\\modul4\\Demo_Repository_JPA-master\\Demo_Repository_JPA-master\\src\\main\\webapp\\WEB-INF/"+ student.getImg();
            System.out.println(nameDelete1);
            File file = new File(nameDelete1);
            file.delete();
            try {
                FileCopyUtils.copy(upImg.getBytes(), new File("E:\\Tu\\modul4\\Demo_Repository_JPA-master\\Demo_Repository_JPA-master\\src\\main\\webapp\\WEB-INF\\img\\" + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String urlImg = "/img/"+ fileName;
            student.setImg(urlImg);
        }
        studentService.save(student);
        ModelAndView modelAndView = new ModelAndView("redirect:/students");
        return modelAndView;
    }
}

package web.CRUDvBoot.сontroller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import web.CRUDvBoot.model.User;
import web.CRUDvBoot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping//  страница по умолчанию где все юзеры
    public String allUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/new")//при нажатии на кнопку "Create new user" обрабатывается гет запрос по адресу /new
    public String createUserForm(@ModelAttribute("user") User user) {
        System.out.println("new user");
        return "user-create";
    }
    /*     Метод createUserForm отвечает за отображение формы для создания нового пользователя.
     *     Метод createUser обрабатывает данные, введенные пользователем в этой форме.
     *   Эти два метода работают вместе в рамках одного процесса — сначала пользователь видит форму (GET-запрос), а
     * затем отправляет данные из этой формы (POST-запрос).
     *
     */

    @PostMapping// Метод createUser в контроллере обрабатывает POST-запрос, который отправляется из формы(user-create)
    public String createUser(@ModelAttribute("user") @Valid User user,            // на веб-странице.
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user-create";
        }
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/edit") //при нажатии кнопки Edit из user-list из формы вызывается метод editUserForm
    public String editUserForm(@RequestParam("id") Long id, Model model) {
        Optional<User> userById = userService.findById(id);

        if (userById.isPresent()) {
            model.addAttribute("user", userById.get());
            return "edit-user";
        } else {
            return "redirect:/users";
        }
    }

    /*Метод обрабатывает POST-запрос по адресу /users/edit, который отправляется из формы(edit-user),
     * когда пользователь нажимает кнопку "update user".
     */
    @PostMapping("/edit")
    public String editUser(@ModelAttribute("user") @Valid User user,//Аннотация @Valid инициирует валидацию данных.
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-user";
        }

        userService.updateUser(user);
        return "redirect:/users";
    }

    @PostMapping("/delete")  //метод вызывается при нажатии кнопки  Delete из user-list
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}

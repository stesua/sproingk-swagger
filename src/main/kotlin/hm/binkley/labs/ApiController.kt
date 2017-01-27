package hm.binkley.labs

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
open class ApiController {
    @RequestMapping("/", method = arrayOf(GET))
    open fun api() = "forward:swagger-ui.html"
}

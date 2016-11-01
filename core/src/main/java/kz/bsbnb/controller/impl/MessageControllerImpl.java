package kz.bsbnb.controller.impl;

import kz.bsbnb.controller.IMessageController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ruslan on 21.10.2016
 */
@RestController
@RequestMapping(value = "/message")
public class MessageControllerImpl implements IMessageController {

}

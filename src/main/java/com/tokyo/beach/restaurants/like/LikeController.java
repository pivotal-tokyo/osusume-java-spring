package com.tokyo.beach.restaurants.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@RestController
public class LikeController {
    private LikeDataMapper likeDataMapper;

    @Autowired
    public LikeController(LikeDataMapper likeDataMapper) {
        this.likeDataMapper = likeDataMapper;
    }

    @RequestMapping(value = "/restaurants/{restaurantId}/likes", method = POST)
    @ResponseStatus(CREATED)
    public Like create(@PathVariable long restaurantId) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        Number userId = (Number) request.getAttribute("userId");

        return likeDataMapper.create(userId.longValue(), restaurantId);
    }

    @RequestMapping(value = "restaurants/{restaurantId}/likes", method = DELETE)
    @ResponseStatus(OK)
    public void delete(@PathVariable long restaurantId) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        Number userId = (Number) request.getAttribute("userId");

        likeDataMapper.delete(userId.longValue(), restaurantId);
    }

}

package com.eshop.mall.product.web;

import com.eshop.mall.product.entity.CategoryEntity;
import com.eshop.mall.product.service.CategoryService;
import com.eshop.mall.product.vo.Catalog2VO;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author ruomengjiang
 * @Date 2022/6/15
 * @Description : eshop-mall
 * 商城主页
 * @Version: 1.0
 */
@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping({"/","/home","/index","/home.html","/index.html"})
    public String index(Model model){
        List<CategoryEntity> list = categoryService.getLeve1Category();
        model.addAttribute("categorys",list);
        return "index";
    }

    @ResponseBody
    @RequestMapping("/index/catalog.json")
    public Map<String, List<Catalog2VO>> getCatalog2JSON(){
        Map<String, List<Catalog2VO>> map = categoryService.getCatelog2JSON();
        return map;
    }

    /**
     * 1.锁会自动续期，如果业务时间超长，运行期间Redisson会自动给锁重新添加30s，
     *   不用担心业务时间，锁自动过去而造成的数据安全问题
     * 2.加锁的业务只要执行完成， 那么就不会给当前的锁续期，
     *   即使不去主动的释放锁，锁在默认30s之后也会自动的删除
     * @return
     */
    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        RLock myLock = redissonClient.getLock("myLock");
        // 加锁
        myLock.lock();
        //如果给定过期时间10s 业务时间超过10s后 自动续期就不会生效
        //实际开发中最好指定过期时间  --> 性能角度考虑
        //myLock.lock(10, TimeUnit.SECONDS);
        try {
            System.out.println("加锁成功...业务处理....." + Thread.currentThread().getName());
            Thread.sleep(30000);
        }catch (Exception e){

        }finally {
            System.out.println("释放锁成功..." +  Thread.currentThread().getName());
            // 释放锁
            myLock.unlock();
        }
        return "hello";
    }

    /**
     * 读写锁 写锁
     * 读 读：相当于没有加锁
     * 写 读：需要等待写锁释放
     * 写 写：阻塞的方式
     * 读 写：读数据的时候会加锁 写的行为也会阻塞
     * 只要有写的行为存在，那么就是一个互斥锁(排他锁)
     * @return
     */
    @GetMapping("/writer")
    @ResponseBody
    public String writerValue(){
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("rw-lock");
        // 加写锁
        RLock rLock = readWriteLock.writeLock();
        String s = null;
        rLock.lock(); // 加写锁
        try {
            s = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set("msg",s);
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return s;
    }

    /**
     * 读写锁 读锁
     * 读读的行为是共享锁，相互之间不影响，读读相当于没有加锁
     * @return
     */
    @GetMapping("/reader")
    @ResponseBody
    public String readValue(){
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("rw-lock");
        // 加读锁
        RLock rLock = readWriteLock.readLock();
        rLock.lock();
        String s = null;
        try {
            s = stringRedisTemplate.opsForValue().get("msg");
        }finally {
            rLock.unlock();
        }

        return s;
    }

    /**
     * 闭锁example
     * @return
     */
    @GetMapping("/lockDoor")
    @ResponseBody
    public String lockDoor(){
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.trySetCount(5);
        try {
            door.await(); // 等待数量降低到0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "bye...";
    }

    @GetMapping("/goHome/{id}")
    @ResponseBody
    public String goHome(@PathVariable Long id){
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown(); // 递减的操作
        return id + "go home";
    }

    /**
     * 信号量处理example
     */
    @GetMapping("/park")
    @ResponseBody
    public String park(){
        RSemaphore park = redissonClient.getSemaphore("park");
        boolean b = true;
        try {
            // park.acquire(); // 获取信号 阻塞到获取成功
            b = park.tryAcquire();// 返回获取成功还是失败
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "停车是否成功:" + b;
    }

    /**
     * 信号量处理example
     */
    @GetMapping("/release")
    @ResponseBody
    public String release(){
        RSemaphore park = redissonClient.getSemaphore("park");
        park.release();
        return "释放了一个车位";
    }

}

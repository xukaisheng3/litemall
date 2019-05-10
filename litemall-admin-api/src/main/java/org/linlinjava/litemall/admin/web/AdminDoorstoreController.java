package org.linlinjava.litemall.admin.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.LitemallBrand;
import org.linlinjava.litemall.db.domain.LitemallDoorstore;
import org.linlinjava.litemall.db.domain.LitemallRole;
import org.linlinjava.litemall.db.service.LitemallBrandService;
import org.linlinjava.litemall.db.service.LitemallDoorstoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/admin/doorstore")
@Validated
public class AdminDoorstoreController {
    private final Log logger = LogFactory.getLog(AdminDoorstoreController.class);

    @Autowired
    private LitemallDoorstoreService doorstoreService;

    @RequiresPermissions("admin:doorstore:list")
    @RequiresPermissionsDesc(menu={"商场管理" , "门店管理"}, button="查询")
    @GetMapping("/list")
    public Object list(String id, String name,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<LitemallDoorstore> brandList = doorstoreService.querySelective(id, name, page, limit, sort, order);
        long total = PageInfo.of(brandList).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", brandList);

        return ResponseUtil.ok(data);
    }

    private Object validate(LitemallDoorstore brand) {
        String name = brand.getDoorstoreName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }

        String desc = brand.getDoorstoreAddress();
        if (StringUtils.isEmpty(desc)) {
            return ResponseUtil.badArgument();
        }

        return null;
    }

    @RequiresPermissions("admin:doorstore:create")
    @RequiresPermissionsDesc(menu={"商场管理" , "门店管理"}, button="添加")
    @PostMapping("/create")
    public Object create(@RequestBody LitemallDoorstore brand) {
        Object error = validate(brand);
        if (error != null) {
            return error;
        }
        doorstoreService.add(brand);
        return ResponseUtil.ok(brand);
    }

    @GetMapping("/options")
    public Object options(){
        List<LitemallDoorstore> roleList = doorstoreService.queryAll();

        List<Map<String, Object>> options = new ArrayList<>(roleList.size());
        for (LitemallDoorstore role : roleList) {
            Map<String, Object> option = new HashMap<>(2);
            option.put("value", role.getId());
            option.put("label", role.getDoorstoreName());
            options.add(option);
        }

        return ResponseUtil.ok(options);
    }


    @RequiresPermissions("admin:doorstore:read")
    @RequiresPermissionsDesc(menu={"商场管理" , "门店管理"}, button="详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        LitemallDoorstore brand = doorstoreService.findById(id);
        return ResponseUtil.ok(brand);
    }

    @RequiresPermissions("admin:doorstore:update")
    @RequiresPermissionsDesc(menu={"商场管理" , "门店管理"}, button="编辑")
    @PostMapping("/update")
    public Object update(@RequestBody LitemallDoorstore brand) {
        Object error = validate(brand);
        if (error != null) {
            return error;
        }
        if (doorstoreService.updateById(brand) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(brand);
    }

    @RequiresPermissions("admin:doorstore:delete")
    @RequiresPermissionsDesc(menu={"商场管理" , "门店管理"}, button="删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody LitemallDoorstore brand) {
        Long id = brand.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }
        doorstoreService.deleteById(id.intValue());
        return ResponseUtil.ok();
    }

}

package org.linlinjava.litemall.db.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.LitemallDoorstoreMapper;
import org.linlinjava.litemall.db.domain.LitemallDoorstore;
import org.linlinjava.litemall.db.domain.LitemallDoorstore.Column;
import org.linlinjava.litemall.db.domain.LitemallDoorstoreExample;
import org.linlinjava.litemall.db.domain.LitemallRole;
import org.linlinjava.litemall.db.domain.LitemallRoleExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;

@Service
public class LitemallDoorstoreService {
    @Resource
    private LitemallDoorstoreMapper doorstoreMapper;
    private Column[] columns = new Column[]{Column.id, Column.doorstoreName, Column.doorstoreAddress, Column.doorstoreAddress, Column.adCode};

    public List<LitemallDoorstore> queryVO(int offset, int limit) {
        LitemallDoorstoreExample example = new LitemallDoorstoreExample();
        example.or().andDeletedEqualTo(false);
        example.setOrderByClause("add_time desc");
        PageHelper.startPage(offset, limit);
        return doorstoreMapper.selectByExampleSelective(example, columns);
    }

    public int queryTotalCount() {
        LitemallDoorstoreExample example = new LitemallDoorstoreExample();
        example.or().andDeletedEqualTo(false);
        return (int) doorstoreMapper.countByExample(example);
    }

    public LitemallDoorstore findById(Integer id) {
        return doorstoreMapper.selectByPrimaryKey(Long.valueOf(id));
    }

    public List<LitemallDoorstore> querySelective(String id, String name, Integer page, Integer size, String sort, String order) {
        LitemallDoorstoreExample example = new LitemallDoorstoreExample();
        LitemallDoorstoreExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(id)) {
            criteria.andIdEqualTo(Long.valueOf(id));
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andDoorstoreNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return doorstoreMapper.selectByExample(example);
    }

    public int updateById(LitemallDoorstore brand) {
        brand.setUpdateTime(LocalDateTime.now());
        return doorstoreMapper.updateByPrimaryKeySelective(brand);
    }

    public void deleteById(Integer id) {
        doorstoreMapper.logicalDeleteByPrimaryKey(Long.valueOf(id));
    }

    public void add(LitemallDoorstore brand) {
        brand.setAddTime(LocalDateTime.now());
        brand.setUpdateTime(LocalDateTime.now());
        doorstoreMapper.insertSelective(brand);
    }

    public List<LitemallDoorstore> all() {
        LitemallDoorstoreExample example = new LitemallDoorstoreExample();
        example.or().andDeletedEqualTo(false);
        return doorstoreMapper.selectByExample(example);
    }

    public List<LitemallDoorstore> queryAll() {
        LitemallDoorstoreExample example = new LitemallDoorstoreExample();
        example.or().andDeletedEqualTo(false);
        return doorstoreMapper.selectByExample(example);
    }
    public List<LitemallDoorstore> queryAllByDoorstoreIds(Integer[] doorstoreIds) {
        LitemallDoorstoreExample example = new LitemallDoorstoreExample();
        LitemallDoorstoreExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isEmpty(doorstoreIds)){
            List<Long> ss=new ArrayList<>();
            ss.add(99999999L);
            criteria.andIdIn(ss);
        }
        if (!StringUtils.isEmpty(doorstoreIds)) {
           List<Integer> ss= Stream.of(doorstoreIds).collect(Collectors.toList());
            criteria.andIdIn(ss.stream().map(a -> Long.parseLong(a.toString())).collect(Collectors.toList()));
        }
        criteria.andDeletedEqualTo(false);
      //  example.or().andDeletedEqualTo(false);
        return doorstoreMapper.selectByExample(example);
    }
}

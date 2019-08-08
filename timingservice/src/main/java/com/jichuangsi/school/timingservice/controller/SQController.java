package com.jichuangsi.school.timingservice.controller;

import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.entity.Img;
import com.jichuangsi.school.timingservice.entity.People;
import com.jichuangsi.school.timingservice.entity.SQFlie;
import com.jichuangsi.school.timingservice.model.ResponseModel;
import com.jichuangsi.school.timingservice.model.SQFlieModel2;
import com.jichuangsi.school.timingservice.service.PeopleService;
import com.jichuangsi.school.timingservice.service.SQService;
import com.jichuangsi.school.timingservice.utils.Byte2File;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sq")
@CrossOrigin
@Api("申诉相关的api")
public class SQController {
    @Resource
    private SQService sqService;
    @Resource
    private PeopleService peopleService;

    @Value("${com.jichuangsi.school.file}")
    private String filePath;

    @ApiOperation(value = "申诉记录入口", notes = "")
    @PostMapping("/sqqq")
    public ResponseModel<String> SQ(@RequestParam String openId,@RequestParam String ruleId,@RequestParam String msg) throws IOException {
        ResponseModel<String> stringResponseModel = new ResponseModel<>();
        SQFlie sq = sqService.getSQ(openId, ruleId);
        if (sq!=null){
            stringResponseModel.setCode(ResultCode.SOS);
            return stringResponseModel;
        }
        String uuid=UUID.randomUUID().toString();
        sqService.saveFile(openId,uuid,ruleId,msg);

        stringResponseModel.setCode(ResultCode.SUCESS);
        stringResponseModel.setData(uuid);
        return stringResponseModel;
    }
    @ApiOperation(value = "申诉附件入口", notes = "")
    @PostMapping("/savefile")
    public ResponseModel<String> saveFile(@RequestParam MultipartFile file,@RequestParam String uuid) throws IOException {
        sqService.saveImg(file.getBytes(),uuid);
        ResponseModel<String> stringResponseModel = new ResponseModel<>();
        stringResponseModel.setCode(ResultCode.SUCESS);
        return stringResponseModel;
    }

    @ApiOperation(value = "根据uuid查图片", notes = "")
    @PostMapping("/getimg")
    public ResponseModel<List<File>> SQGetImg( @RequestParam String uuid) throws FileNotFoundException {

        List<File> files = new ArrayList<>();
        List<Img> f=sqService.getImgList(uuid);
            for (Img img:f
                 ) {
                files.add(Byte2File.getFile(img.getImg(),filePath,UUID.randomUUID().toString()+".jpg"));
            }
        ResponseModel<List<File>> odel = new ResponseModel<>();
        odel.setData(files);
        odel.setCode(ResultCode.SUCESS);
        return odel;

    }
    @ApiOperation(value = "根据openid查询申诉记录", notes = "")
    @PostMapping("/getsqrecord")
    public ResponseModel<List<SQFlie>> SQGetFile(@RequestParam String openId)  {
        List<SQFlie> file = sqService.getFile(openId);
        ResponseModel<List<SQFlie>> odel = new ResponseModel<>();
        odel.setData(file);
        odel.setCode(ResultCode.SUCESS);
        return odel;
    }
    @ApiOperation(value = "查看全部未审批申诉记录", notes = "")
    @PostMapping("/getAllUnapprovedSQ")
    public ResponseModel<List<SQFlieModel2>> SQAllUnapproved(@RequestParam @Nullable String name)  {
if (name==null||name.equalsIgnoreCase("")){
    List<SQFlie> file = sqService.getAllUnapproved();
    List<SQFlieModel2> sqFlieModel2s = new ArrayList<>();
    ResponseModel<List<SQFlieModel2>> odel = new ResponseModel<>();
    for (SQFlie sqFlie:file
            ) {
        SQFlieModel2 sqFlieModel2 = new SQFlieModel2();

        sqFlieModel2.setPeople(peopleService.findPeople(sqFlie.getOpenId()));
        sqFlieModel2.setSqFlie(sqFlie);
        sqFlieModel2s.add(sqFlieModel2);
    }

    odel.setData(sqFlieModel2s);
    odel.setCode(ResultCode.SUCESS);
    return odel;
}
        else{
            List<People> allPeople = peopleService.findAllPeople(name);

            List<SQFlieModel2> sqFlieModel2s = new ArrayList<>();
            for (People people:allPeople
                    ) {
                List<SQFlie> file =sqService.getUnapprovedSQForName(people.getOpenId());
                for (SQFlie sqFlie:file
                        ) {
                    SQFlieModel2 sqFlieModel2 = new SQFlieModel2();

                    sqFlieModel2.setPeople(peopleService.findPeople(sqFlie.getOpenId()));
                    sqFlieModel2.setSqFlie(sqFlie);
                    sqFlieModel2s.add(sqFlieModel2);
                }

            }
            ResponseModel<List<SQFlieModel2>> odel = new ResponseModel<>();
            odel.setData(sqFlieModel2s);
            odel.setCode(ResultCode.SUCESS);
            return odel;
        }
    }


    @ApiOperation(value = "查看全部已审批申诉记录", notes = "")
    @PostMapping("/getAllapprovedSQForName")
    public ResponseModel<List<SQFlieModel2>> SQAllapprovedforname(@RequestParam @Nullable String name)  {
        if (name==null||name.equalsIgnoreCase("")){
            List<SQFlie> file = sqService.getAllapproved();
            List<SQFlieModel2> sqFlieModel2s = new ArrayList<>();
            ResponseModel<List<SQFlieModel2>> odel = new ResponseModel<>();
            for (SQFlie sqFlie:file
                    ) {
                SQFlieModel2 sqFlieModel2 = new SQFlieModel2();

                sqFlieModel2.setPeople(peopleService.findPeople(sqFlie.getOpenId()));
                sqFlieModel2.setSqFlie(sqFlie);
                sqFlieModel2s.add(sqFlieModel2);
            }

            odel.setData(sqFlieModel2s);
            odel.setCode(ResultCode.SUCESS);
            return odel;
        }else {
            List<People> allPeople = peopleService.findAllPeople(name);

            List<SQFlieModel2> sqFlieModel2s = new ArrayList<>();
            for (People people:allPeople
                    ) {
                List<SQFlie> file =sqService.getapprovedSQForName(people.getOpenId());
                for (SQFlie sqFlie:file
                        ) {
                    SQFlieModel2 sqFlieModel2 = new SQFlieModel2();

                    sqFlieModel2.setPeople(peopleService.findPeople(sqFlie.getOpenId()));
                    sqFlieModel2.setSqFlie(sqFlie);
                    sqFlieModel2s.add(sqFlieModel2);
                }

            }
            ResponseModel<List<SQFlieModel2>> odel = new ResponseModel<>();
            odel.setData(sqFlieModel2s);
            odel.setCode(ResultCode.SUCESS);
            return odel;
        }
    }
}

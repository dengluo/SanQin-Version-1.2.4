package com.pbids.sanqin.model.entity;

import android.util.Log;

import com.pbids.sanqin.utils.FormatDateUtils;
import com.pbids.sanqin.utils.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * 文章对象
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:43
 * @desscribe 类描述:文章对象实体类
 * @remark 备注:关联了greedgao，为可插入数据库的实体类
 * @see
 */
@Entity
public class NewsArticle {
    @Transient
    public final static int ONE_TYPE = 1;
    @Transient
    public final static int TWO_TYPE = 2;
    @Transient
    public final static int THREE_TYPE = 3;
    @Transient
    public final static int FOUR_TYPE = 4;
    @Transient
    public final static int FIVE_TYPE = 5;
    @Transient
    public final static int SIX_TYPE = 6;
    @Transient
    public final static int SEVEN_TYPE = 7;
    @Transient
    public final static int EIGHT_TYPE = 8;
    @Transient
    public final static int NINE_TYPE = 9;
    @Transient
    public final static int FOURTEEN_TYPE = 14;
    @Transient
    public final static int TWENTY_TYPE = 20;
    @Transient
    public final static int TWENTY_ONE_TYPE = 21;

    @Transient
    public String body = "";
    @Transient
    private long startTime;  //开始时间
    @Transient
    private long overTime;   //结束时间
    @Transient
    private int pepleNum; //人数


    @Id
    private Long _id;
    private long aid;
    private int arctype;
    private int channel;
    private int clickNum; //点击量
    private long createTime;
    private int fromNum; //转发数量
    private String icon;
    private long id;
    private String link;
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> litpicList;
    private int mediatype;
    private int rewordNum;
    private int state;
    private String subTitle;
    private String tagLink;
    private String tags;
    private String title;
    private int view;
    private String writer;
    private long attendTime;
    private int reviewed;
    private int finish;
    private long browseTime;
    private String surname;
    private int isPay = 0;
    private String redirectUrl;
    private boolean redirect;

    private String organization="";
    private String surnameIcon="";

    @Generated(hash = 210131438)
    public NewsArticle(Long _id, long aid, int arctype, int channel, int clickNum,
            long createTime, int fromNum, String icon, long id, String link,
            List<String> litpicList, int mediatype, int rewordNum, int state,
            String subTitle, String tagLink, String tags, String title, int view,
            String writer, long attendTime, int reviewed, int finish,
            long browseTime, String surname, int isPay, String redirectUrl,
            boolean redirect, String organization, String surnameIcon) {
        this._id = _id;
        this.aid = aid;
        this.arctype = arctype;
        this.channel = channel;
        this.clickNum = clickNum;
        this.createTime = createTime;
        this.fromNum = fromNum;
        this.icon = icon;
        this.id = id;
        this.link = link;
        this.litpicList = litpicList;
        this.mediatype = mediatype;
        this.rewordNum = rewordNum;
        this.state = state;
        this.subTitle = subTitle;
        this.tagLink = tagLink;
        this.tags = tags;
        this.title = title;
        this.view = view;
        this.writer = writer;
        this.attendTime = attendTime;
        this.reviewed = reviewed;
        this.finish = finish;
        this.browseTime = browseTime;
        this.surname = surname;
        this.isPay = isPay;
        this.redirectUrl = redirectUrl;
        this.redirect = redirect;
        this.organization = organization;
        this.surnameIcon = surnameIcon;
    }

    @Generated(hash = 1460512480)
    public NewsArticle() {
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getTags() {
//        if(tags!=null){
//            if(!"".equals(surname.trim()) && surname.length()>0){
//                tags = surname+","+tags;
//            }else{
//                tags = surname;
//            }
//        }
//        Log.i("wzh","tags: "+tags);
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getArctype() {
        return arctype;
    }

    public void setArctype(int arctype) {
        this.arctype = arctype;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getFromNum() {
        return fromNum;
    }

    public void setFromNum(int fromNum) {
        this.fromNum = fromNum;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getLitpicList() {
        return litpicList;
    }

    public void setLitpicList(ArrayList<String> litpicList) {
        this.litpicList = litpicList;
    }

    public int getMediatype() {
        return mediatype;
    }

    public void setMediatype(int mediatype) {
        this.mediatype = mediatype;
    }

    public int getRewordNum() {
        return rewordNum;
    }

    public void setRewordNum(int rewordNum) {
        this.rewordNum = rewordNum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTagLink() {
        return tagLink;
    }

    public void setTagLink(String tagLink) {
        this.tagLink = tagLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public int getViewType() {
        int pictureSize = litpicList.size();
        //Log.i("wzw","arctype: "+arctype);
        switch (arctype){
            case 12:
//                if(pictureSize>0){
                    switch (pictureSize){
                        case 1:
                            view = 1;
                            break;
                        case 2:
                            view = 2;
                            break;
                        case 3:
                            view = 3;
                            break;
                        default:
                            view = 1;
                            break;
                    }
//                }else{
//                    view_donate_records = 1;
//                }
                break;
            case 13:
                view = 5;
                break;
            case 14:
                view = 4;
                break;
            case 15:
                view = 21;
                break;
            case 21:
                switch (pictureSize){
                    case 1:
                        view = 7;
                        break;
                    case 2:
                        view = 7;
                        break;
                    case 3:
                        view = 9;
                        break;
                    default:
                        view =7;
                        break;
                }
                break;
            case 22:
                view = 14;
                break;
        }
        return view;
    }

    public void setViewType(int view) {
        this.view = view;
    }

    public long getAttendTime() {
        return attendTime;
    }

    public void setAttendTime(long attendTime) {
        this.attendTime = attendTime;
    }

    public int getReviewed() {
        return reviewed;
    }

    public void setReviewed(int reviewed) {
        this.reviewed = reviewed;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public void setLitpicList(List<String> litpicList) {
        this.litpicList = litpicList;
    }

    public long getBrowseTime() {
        return this.browseTime;
    }

    public void setBrowseTime(long browseTime) {
        this.browseTime = browseTime;
    }

    public int getView() {
        return this.view;
    }

    public void setView(int view) {
        this.view = view;
    }

    @Override
    public String toString() {
        return "NewsArticle{" +
                "_id=" + _id +
                ", arctype=" + arctype +
                ", channel=" + channel +
                ", clickNum=" + clickNum +
                ", createTime=" + createTime +
                ", fromNum=" + fromNum +
                ", icon='" + icon + '\'' +
                ", id=" + id +
                ", link='" + link + '\'' +
                ", litpicList=" + litpicList +
                ", mediatype=" + mediatype +
                ", rewordNum=" + rewordNum +
                ", state=" + state +
                ", subTitle='" + subTitle + '\'' +
                ", tagLink='" + tagLink + '\'' +
                ", tags='" + tags + '\'' +
                ", title='" + title + '\'' +
                ", view_donate_records=" + view +
                ", writer='" + writer + '\'' +
                ", attendTime=" + attendTime +
                ", reviewed=" + reviewed +
                ", finish=" + finish +
                ", browseTime=" + browseTime +
                ", surname='" + surname + '\'' +
                ", isPay=" + isPay +
                '}';
    }

    public String getSurnameIcon() {
        return surnameIcon;
    }

    public void setSurnameIcon(String surnameIcon) {
        this.surnameIcon = surnameIcon;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public long getStartTime() {
        return startTime;
    }
    public String getStartTimeFormat() {
        if (this.startTime >0 ) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            String dateString = formatter.format(this.startTime);
            return dateString;
        }
        return "";
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getOverTime() {
        return overTime;
    }
    public String getOverTimeFormat() {
        if (this.overTime >0 ) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            String dateString = formatter.format(this.overTime);
            return dateString;
        }
        return "";
    }

    public void setOverTime(long overTime) {
        this.overTime = overTime;
    }

    public int getPepleNum() {
        return pepleNum;
    }

    public void setPepleNum(int pepleNum) {
        this.pepleNum = pepleNum;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public boolean getRedirect() {
        return this.redirect;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }
}

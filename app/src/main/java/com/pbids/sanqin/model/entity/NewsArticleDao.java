package com.pbids.sanqin.model.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.pbids.sanqin.utils.StringConverter;
import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NEWS_ARTICLE".
*/
public class NewsArticleDao extends AbstractDao<NewsArticle, Long> {

    public static final String TABLENAME = "NEWS_ARTICLE";

    /**
     * Properties of entity NewsArticle.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property Aid = new Property(1, long.class, "aid", false, "AID");
        public final static Property Arctype = new Property(2, int.class, "arctype", false, "ARCTYPE");
        public final static Property Channel = new Property(3, int.class, "channel", false, "CHANNEL");
        public final static Property ClickNum = new Property(4, int.class, "clickNum", false, "CLICK_NUM");
        public final static Property CreateTime = new Property(5, long.class, "createTime", false, "CREATE_TIME");
        public final static Property FromNum = new Property(6, int.class, "fromNum", false, "FROM_NUM");
        public final static Property Icon = new Property(7, String.class, "icon", false, "ICON");
        public final static Property Id = new Property(8, long.class, "id", false, "ID");
        public final static Property Link = new Property(9, String.class, "link", false, "LINK");
        public final static Property LitpicList = new Property(10, String.class, "litpicList", false, "LITPIC_LIST");
        public final static Property Mediatype = new Property(11, int.class, "mediatype", false, "MEDIATYPE");
        public final static Property RewordNum = new Property(12, int.class, "rewordNum", false, "REWORD_NUM");
        public final static Property State = new Property(13, int.class, "state", false, "STATE");
        public final static Property SubTitle = new Property(14, String.class, "subTitle", false, "SUB_TITLE");
        public final static Property TagLink = new Property(15, String.class, "tagLink", false, "TAG_LINK");
        public final static Property Tags = new Property(16, String.class, "tags", false, "TAGS");
        public final static Property Title = new Property(17, String.class, "title", false, "TITLE");
        public final static Property View = new Property(18, int.class, "view", false, "VIEW");
        public final static Property Writer = new Property(19, String.class, "writer", false, "WRITER");
        public final static Property AttendTime = new Property(20, long.class, "attendTime", false, "ATTEND_TIME");
        public final static Property Reviewed = new Property(21, int.class, "reviewed", false, "REVIEWED");
        public final static Property Finish = new Property(22, int.class, "finish", false, "FINISH");
        public final static Property BrowseTime = new Property(23, long.class, "browseTime", false, "BROWSE_TIME");
        public final static Property Surname = new Property(24, String.class, "surname", false, "SURNAME");
        public final static Property IsPay = new Property(25, int.class, "isPay", false, "IS_PAY");
        public final static Property RedirectUrl = new Property(26, String.class, "redirectUrl", false, "REDIRECT_URL");
        public final static Property Redirect = new Property(27, boolean.class, "redirect", false, "REDIRECT");
        public final static Property Organization = new Property(28, String.class, "organization", false, "ORGANIZATION");
        public final static Property SurnameIcon = new Property(29, String.class, "surnameIcon", false, "SURNAME_ICON");
    }

    private final StringConverter litpicListConverter = new StringConverter();

    public NewsArticleDao(DaoConfig config) {
        super(config);
    }
    
    public NewsArticleDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NEWS_ARTICLE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: _id
                "\"AID\" INTEGER NOT NULL ," + // 1: aid
                "\"ARCTYPE\" INTEGER NOT NULL ," + // 2: arctype
                "\"CHANNEL\" INTEGER NOT NULL ," + // 3: channel
                "\"CLICK_NUM\" INTEGER NOT NULL ," + // 4: clickNum
                "\"CREATE_TIME\" INTEGER NOT NULL ," + // 5: createTime
                "\"FROM_NUM\" INTEGER NOT NULL ," + // 6: fromNum
                "\"ICON\" TEXT," + // 7: icon
                "\"ID\" INTEGER NOT NULL ," + // 8: id
                "\"LINK\" TEXT," + // 9: link
                "\"LITPIC_LIST\" TEXT," + // 10: litpicList
                "\"MEDIATYPE\" INTEGER NOT NULL ," + // 11: mediatype
                "\"REWORD_NUM\" INTEGER NOT NULL ," + // 12: rewordNum
                "\"STATE\" INTEGER NOT NULL ," + // 13: state
                "\"SUB_TITLE\" TEXT," + // 14: subTitle
                "\"TAG_LINK\" TEXT," + // 15: tagLink
                "\"TAGS\" TEXT," + // 16: tags
                "\"TITLE\" TEXT," + // 17: title
                "\"VIEW\" INTEGER NOT NULL ," + // 18: view
                "\"WRITER\" TEXT," + // 19: writer
                "\"ATTEND_TIME\" INTEGER NOT NULL ," + // 20: attendTime
                "\"REVIEWED\" INTEGER NOT NULL ," + // 21: reviewed
                "\"FINISH\" INTEGER NOT NULL ," + // 22: finish
                "\"BROWSE_TIME\" INTEGER NOT NULL ," + // 23: browseTime
                "\"SURNAME\" TEXT," + // 24: surname
                "\"IS_PAY\" INTEGER NOT NULL ," + // 25: isPay
                "\"REDIRECT_URL\" TEXT," + // 26: redirectUrl
                "\"REDIRECT\" INTEGER NOT NULL ," + // 27: redirect
                "\"ORGANIZATION\" TEXT," + // 28: organization
                "\"SURNAME_ICON\" TEXT);"); // 29: surnameIcon
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NEWS_ARTICLE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NewsArticle entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindLong(2, entity.getAid());
        stmt.bindLong(3, entity.getArctype());
        stmt.bindLong(4, entity.getChannel());
        stmt.bindLong(5, entity.getClickNum());
        stmt.bindLong(6, entity.getCreateTime());
        stmt.bindLong(7, entity.getFromNum());
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(8, icon);
        }
        stmt.bindLong(9, entity.getId());
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(10, link);
        }
 
        List litpicList = entity.getLitpicList();
        if (litpicList != null) {
            stmt.bindString(11, litpicListConverter.convertToDatabaseValue(litpicList));
        }
        stmt.bindLong(12, entity.getMediatype());
        stmt.bindLong(13, entity.getRewordNum());
        stmt.bindLong(14, entity.getState());
 
        String subTitle = entity.getSubTitle();
        if (subTitle != null) {
            stmt.bindString(15, subTitle);
        }
 
        String tagLink = entity.getTagLink();
        if (tagLink != null) {
            stmt.bindString(16, tagLink);
        }
 
        String tags = entity.getTags();
        if (tags != null) {
            stmt.bindString(17, tags);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(18, title);
        }
        stmt.bindLong(19, entity.getView());
 
        String writer = entity.getWriter();
        if (writer != null) {
            stmt.bindString(20, writer);
        }
        stmt.bindLong(21, entity.getAttendTime());
        stmt.bindLong(22, entity.getReviewed());
        stmt.bindLong(23, entity.getFinish());
        stmt.bindLong(24, entity.getBrowseTime());
 
        String surname = entity.getSurname();
        if (surname != null) {
            stmt.bindString(25, surname);
        }
        stmt.bindLong(26, entity.getIsPay());
 
        String redirectUrl = entity.getRedirectUrl();
        if (redirectUrl != null) {
            stmt.bindString(27, redirectUrl);
        }
        stmt.bindLong(28, entity.getRedirect() ? 1L: 0L);
 
        String organization = entity.getOrganization();
        if (organization != null) {
            stmt.bindString(29, organization);
        }
 
        String surnameIcon = entity.getSurnameIcon();
        if (surnameIcon != null) {
            stmt.bindString(30, surnameIcon);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NewsArticle entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindLong(2, entity.getAid());
        stmt.bindLong(3, entity.getArctype());
        stmt.bindLong(4, entity.getChannel());
        stmt.bindLong(5, entity.getClickNum());
        stmt.bindLong(6, entity.getCreateTime());
        stmt.bindLong(7, entity.getFromNum());
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(8, icon);
        }
        stmt.bindLong(9, entity.getId());
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(10, link);
        }
 
        List litpicList = entity.getLitpicList();
        if (litpicList != null) {
            stmt.bindString(11, litpicListConverter.convertToDatabaseValue(litpicList));
        }
        stmt.bindLong(12, entity.getMediatype());
        stmt.bindLong(13, entity.getRewordNum());
        stmt.bindLong(14, entity.getState());
 
        String subTitle = entity.getSubTitle();
        if (subTitle != null) {
            stmt.bindString(15, subTitle);
        }
 
        String tagLink = entity.getTagLink();
        if (tagLink != null) {
            stmt.bindString(16, tagLink);
        }
 
        String tags = entity.getTags();
        if (tags != null) {
            stmt.bindString(17, tags);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(18, title);
        }
        stmt.bindLong(19, entity.getView());
 
        String writer = entity.getWriter();
        if (writer != null) {
            stmt.bindString(20, writer);
        }
        stmt.bindLong(21, entity.getAttendTime());
        stmt.bindLong(22, entity.getReviewed());
        stmt.bindLong(23, entity.getFinish());
        stmt.bindLong(24, entity.getBrowseTime());
 
        String surname = entity.getSurname();
        if (surname != null) {
            stmt.bindString(25, surname);
        }
        stmt.bindLong(26, entity.getIsPay());
 
        String redirectUrl = entity.getRedirectUrl();
        if (redirectUrl != null) {
            stmt.bindString(27, redirectUrl);
        }
        stmt.bindLong(28, entity.getRedirect() ? 1L: 0L);
 
        String organization = entity.getOrganization();
        if (organization != null) {
            stmt.bindString(29, organization);
        }
 
        String surnameIcon = entity.getSurnameIcon();
        if (surnameIcon != null) {
            stmt.bindString(30, surnameIcon);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public NewsArticle readEntity(Cursor cursor, int offset) {
        NewsArticle entity = new NewsArticle( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.getLong(offset + 1), // aid
            cursor.getInt(offset + 2), // arctype
            cursor.getInt(offset + 3), // channel
            cursor.getInt(offset + 4), // clickNum
            cursor.getLong(offset + 5), // createTime
            cursor.getInt(offset + 6), // fromNum
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // icon
            cursor.getLong(offset + 8), // id
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // link
            cursor.isNull(offset + 10) ? null : litpicListConverter.convertToEntityProperty(cursor.getString(offset + 10)), // litpicList
            cursor.getInt(offset + 11), // mediatype
            cursor.getInt(offset + 12), // rewordNum
            cursor.getInt(offset + 13), // state
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // subTitle
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // tagLink
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // tags
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // title
            cursor.getInt(offset + 18), // view
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // writer
            cursor.getLong(offset + 20), // attendTime
            cursor.getInt(offset + 21), // reviewed
            cursor.getInt(offset + 22), // finish
            cursor.getLong(offset + 23), // browseTime
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // surname
            cursor.getInt(offset + 25), // isPay
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // redirectUrl
            cursor.getShort(offset + 27) != 0, // redirect
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // organization
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29) // surnameIcon
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NewsArticle entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAid(cursor.getLong(offset + 1));
        entity.setArctype(cursor.getInt(offset + 2));
        entity.setChannel(cursor.getInt(offset + 3));
        entity.setClickNum(cursor.getInt(offset + 4));
        entity.setCreateTime(cursor.getLong(offset + 5));
        entity.setFromNum(cursor.getInt(offset + 6));
        entity.setIcon(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setId(cursor.getLong(offset + 8));
        entity.setLink(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setLitpicList(cursor.isNull(offset + 10) ? null : litpicListConverter.convertToEntityProperty(cursor.getString(offset + 10)));
        entity.setMediatype(cursor.getInt(offset + 11));
        entity.setRewordNum(cursor.getInt(offset + 12));
        entity.setState(cursor.getInt(offset + 13));
        entity.setSubTitle(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setTagLink(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setTags(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setTitle(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setView(cursor.getInt(offset + 18));
        entity.setWriter(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setAttendTime(cursor.getLong(offset + 20));
        entity.setReviewed(cursor.getInt(offset + 21));
        entity.setFinish(cursor.getInt(offset + 22));
        entity.setBrowseTime(cursor.getLong(offset + 23));
        entity.setSurname(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setIsPay(cursor.getInt(offset + 25));
        entity.setRedirectUrl(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setRedirect(cursor.getShort(offset + 27) != 0);
        entity.setOrganization(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setSurnameIcon(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(NewsArticle entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(NewsArticle entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(NewsArticle entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

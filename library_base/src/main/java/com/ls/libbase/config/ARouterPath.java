package com.ls.libbase.config;

public final class ARouterPath {
    private ARouterPath() {}

    public static class Main {
        private static final String MAIN = "/main";
        public static final String mainActivity = MAIN + "/mainActivity";
    }

    public static class Home {
        private static final String HOME = "/home";
        public static final String homeFragment = HOME + "/homeFragment";


    }

    public static class Find {
        private static final String FIND = "/find";
        public static final String findFragment = FIND + "/findFragment";
        public static final String ACTIVITY_CATEGORY_DETAIL = FIND + "/CategoryDetailActivity";
        public static final String KEY_CATEGORY_DATA = "KEY_CATEGORY_DATA";

        public static final String ACTIVITY_THEME_LIST = FIND + "/ThemeListActivity";
        public static final String ACTIVITY_TOPIC = FIND + "/TopicActivity";
    }

    public static class Plaza {
        private static final String PLAZA = "/plaza";
        public static final String plazaFragment = PLAZA + "/plazaFragment";

        //图片详情页Activity
        public static final String IMAGE_ACTIVITY = PLAZA + "/imageActivity";
        //图片详情页单图Fragment
        public static final String FRAGMENT_IMAGE_DETAIL = PLAZA + "/imageDetailFragment";
        //图片详情页接收 PlazaDetail 对象的 key（Serializable）
        public static final String KEY_IMAGE_DATA = "KEY_IMAGE_DATA";
        //单图页接收图片 url 的 key
        public static final String KEY_IMAGE_URL = "KEY_IMAGE_URL";
    }

    public static class User {
        private static final String USER = "/user";
        public static final String userFragment = USER + "/userFragment";
        public static final String ACTIVITY_LOGIN = USER + "/loginActivity";
        public static final String ACTIVITY_AGREEMENT = USER + "/agreement";

        //设置页
        public static final String ACTIVITY_SETTINGS = USER + "/SettingsActivity";
        //修改密码
        public static final String ACTIVITY_RESETPWD = USER + "/ResetPasswordActivity";
        //推送设置
        public static final String ACTIVITY_PUSHSETTINGS = USER + "/PushSettingsActivity";
        //播放设置
        public static final String ACTIVITY_PLAYTTINGS = USER + "/PlaySettingsActivity";
        //账户与绑定
        public static final String ACTIVITY_ACCOUNT = USER + "/AccountActivity";
        public static final String ACTIVITY_PERMISSION = USER + "/PermissionActivity";
        public static final String ACTIVITY_EDITUSERINFO = USER + "/EditUserInfoActivity";
        public static final String ACTIVITY_ABOUTME = USER + "/AboutMeActivity";
        //自定义相机页（拍照更换头像）
        public static final String ACTIVITY_CAMERA = USER + "/CameraActivity";

    }

    public static class Video {

        private static final String VIDEO = "/video";
        public static final String ACTIVITY_VIDEODETAIL = VIDEO+"/VideoDetailActivity";
        //视频详情页接收视频id的key
        public static final String KEY_VIDEO_ID = "KEY_VIDEO_ID";

        public static final String FRAGMENT_COMMENT = VIDEO+"/VideoCommentFragment";
        public static final String FRAGMENT_INTRODUCE = VIDEO+"/IntroduceFragment";


        public static final String FRAGMENT_VIDEO_LIST = VIDEO + "/videoListFragment";

        public static final String KEY_VIDEO_LIST_TYPE = "KEY_VIDEO_LIST_TYPE";

        //如果style为true，表示需要把列表页的item文本颜色改成白色
        public static final String KEY_VIDEO_LIST_STYLE = "KEY_VIDEO_LIST_STYLE";

        //视频列表-推荐页
        public static final int VIDEO_LIST_FRAGMENT_RECOMMEND = 0;
        //视频列表-日报页
        public static final int VIDEO_LIST_FRAGMENT_DAILY = 1;

        public static final String FRAGMENT_CATEGORY_LIST = VIDEO + "/CategoryListFragment";

        public static final String KEY_CATEGORY_ID = "KEY_CATEGORY_ID";
        public static final String KEY_CATEGORY_TYPE = "KEY_CATEGORY_TYPE";

        //分类详情中的视频列表-热门推荐
        public static final int CATEGORY_VIDEO_RECOMMEND = 0;
        //分类详情中的视频列表-最新发布
        public static final int CATEGORY_VIDEO_NEWPUBLISH = 1;

        //搜索视频
        public static final String ACTIVITY_SEARCH = VIDEO + "/SearchActivity";

        //收藏页
        public static final String ACTIVITY_COLLECTION = VIDEO + "/CollectionActivity";

        //浏览记录页
        public static final String ACTIVITY_PLAYRECORD = VIDEO + "/PlayerRecordActivity";
    }


}
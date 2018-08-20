package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hpz on 2018/5/23.
 */

public class OTCDiscoveryBean implements Serializable {
    private List<BannerListBean> BannerList;
    private List<ModuleListBean> ModuleList;

    public List<BannerListBean> getBannerList() {
        return BannerList;
    }

    public void setBannerList(List<BannerListBean> BannerList) {
        this.BannerList = BannerList;
    }

    public List<ModuleListBean> getModuleList() {
        return ModuleList;
    }

    public void setModuleList(List<ModuleListBean> ModuleList) {
        this.ModuleList = ModuleList;
    }

    public static class BannerListBean implements Serializable{
        /**
         * Id : 9
         * Title : test banner
         * ImageUrl : http://192.168.1.36:8888/group1/M00/00/01/wKgBJFsFOcGATg1JAAD9LxVwPZ4473.jpg
         * IsHaveUrl : true
         * Url : http://www.baidu.com
         */

        private int Id;
        private String Title;
        private String ImageUrl;
        private boolean IsHaveUrl;
        private String Url;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public boolean isIsHaveUrl() {
            return IsHaveUrl;
        }

        public void setIsHaveUrl(boolean IsHaveUrl) {
            this.IsHaveUrl = IsHaveUrl;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }
    }

    public static class ModuleListBean implements Serializable{
        /**
         * ModuleName : / RACION /
         * DappList : [{"Id":3,"DappName":"test dapp","LogoUrl":"http://192.168.1.36:8888/group1/M00/00/01/wKgBJFsFO4iAaF_tAABck3fveJo750.jpg","Url":"http://www.github.com","Read":false},{"Id":4,"DappName":"dapp eee","LogoUrl":"http://192.168.1.36:8888/group1/M00/00/01/wKgBJFsFO3WAbQyYAABAgdrBsFE357.jpg","Url":"http://www.google.com","Read":false}]
         */

        private String ModuleName;
        private List<DappListBean> DappList;

        public String getModuleName() {
            return ModuleName;
        }

        public void setModuleName(String ModuleName) {
            this.ModuleName = ModuleName;
        }

        public List<DappListBean> getDappList() {
            return DappList;
        }

        public void setDappList(List<DappListBean> DappList) {
            this.DappList = DappList;
        }

        public static class DappListBean implements Serializable{
            /**
             * Id : 3
             * DappName : test dapp
             * LogoUrl : http://192.168.1.36:8888/group1/M00/00/01/wKgBJFsFO4iAaF_tAABck3fveJo750.jpg
             * Url : http://www.github.com
             * Read : false
             */

            private int Id;
            private String DappName;
            private String LogoUrl;
            private String Url;
            private boolean Read;

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getDappName() {
                return DappName;
            }

            public void setDappName(String DappName) {
                this.DappName = DappName;
            }

            public String getLogoUrl() {
                return LogoUrl;
            }

            public void setLogoUrl(String LogoUrl) {
                this.LogoUrl = LogoUrl;
            }

            public String getUrl() {
                return Url;
            }

            public void setUrl(String Url) {
                this.Url = Url;
            }

            public boolean isRead() {
                return Read;
            }

            public void setRead(boolean Read) {
                this.Read = Read;
            }
        }
    }
}

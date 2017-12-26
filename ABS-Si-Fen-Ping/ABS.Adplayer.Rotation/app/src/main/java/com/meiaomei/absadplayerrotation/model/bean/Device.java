package com.meiaomei.absadplayerrotation.model.bean;

/**
 * Created by huyawen on 2017/3/1.
 * 设备实体类
 */

public class Device {

        public String deviceName;// 设备名称
        public String systemType;// 系统类型
        public String firmwareVersion;// 固件版本
        public String appVersion;// 应用程序版本
        public String macAddress;// MAC地址
        public String iPAddress;// IP地址
        public String subNetMask;// 子网掩码
        public String defaultGateway;// 默认网关
        public String outputResolution;// 输出分辨率
        public String hardDiskFreeSpace;// 应用程序所在硬盘可用空间
        public String totalPhysicalMemory;// 总物理存储
        public String LoginUserName;// 操作系统的登录用户名
        public String cpuID ;// cpuId
        public String diskID ;// 硬盘Id


        public String getDeviceName() {
                return deviceName;
        }

        public void setDeviceName(String deviceName) {
                this.deviceName = deviceName;
        }

        public String getSystemType() {
                return systemType;
        }

        public void setSystemType(String systemType) {
                this.systemType = systemType;
        }

        public String getFirmwareVersion() {
                return firmwareVersion;
        }

        public void setFirmwareVersion(String firmwareVersion) {
                this.firmwareVersion = firmwareVersion;
        }

        public String getAppVersion() {
                return appVersion;
        }

        public void setAppVersion(String appVersion) {
                this.appVersion = appVersion;
        }

        public String getMacAddress() {
                return macAddress;
        }

        public void setMacAddress(String macAddress) {
                this.macAddress = macAddress;
        }

        public String getiPAddress() {
                return iPAddress;
        }

        public void setiPAddress(String iPAddress) {
                this.iPAddress = iPAddress;
        }

        public String getSubNetMask() {
                return subNetMask;
        }

        public void setSubNetMask(String subNetMask) {
                this.subNetMask = subNetMask;
        }

        public String getDefaultGateway() {
                return defaultGateway;
        }

        public void setDefaultGateway(String defaultGateway) {
                this.defaultGateway = defaultGateway;
        }

        public String getOutputResolution() {
                return outputResolution;
        }

        public void setOutputResolution(String outputResolution) {
                this.outputResolution = outputResolution;
        }

        public String getHardDiskFreeSpace() {
                return hardDiskFreeSpace;
        }

        public void setHardDiskFreeSpace(String hardDiskFreeSpace) {
                this.hardDiskFreeSpace = hardDiskFreeSpace;
        }

        public String getTotalPhysicalMemory() {
                return totalPhysicalMemory;
        }

        public void setTotalPhysicalMemory(String totalPhysicalMemory) {
                this.totalPhysicalMemory = totalPhysicalMemory;
        }

        public String getLoginUserName() {
                return LoginUserName;
        }

        public void setLoginUserName(String loginUserName) {
                LoginUserName = loginUserName;
        }

        public String getCpuID() {
                return cpuID;
        }

        public void setCpuID(String cpuID) {
                this.cpuID = cpuID;
        }

        public String getDiskID() {
                return diskID;
        }

        public void setDiskID(String diskID) {
                this.diskID = diskID;
        }
}

package flambus.app._enum;

public enum UploadCode {
    REVIEW("리뷰"),
    FEED("피드");

    private String uploadCode;

    UploadCode(String uploadCode) {
        this.uploadCode = uploadCode;
    }

    public String getUploadCode() {
        return this.uploadCode;
    }

    public static UploadCode of(String code) {
        if(code == null) {
            throw new IllegalArgumentException();
        }

        for (UploadCode uc : UploadCode.values()) {
            if(uc.uploadCode.equals(code)) {
                return uc;
            }
        }
        throw new IllegalArgumentException("일치하는 업로드 코드가 없습니다.");
    }
}

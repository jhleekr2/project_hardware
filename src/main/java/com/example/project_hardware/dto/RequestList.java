package com.example.project_hardware.dto;
import org.springframework.data.domain.Pageable;

//페이징 관련 클래스 - 스프링 부트의 페이징 기능 사용할것임
//많은 블로그에서 JPA에만 사용한다고 오도하고 있으나 사실 마이바티스와도 같이 사용가능

public class RequestList<T> {

    private T data;
    private Pageable pageable;

    // DTO 정의
    public RequestList() {
    }

    public RequestList(T data, Pageable pageable) {
        this.data = data;
        this.pageable = pageable;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    @Override
    public String toString() {
        return "RequestList{" +
                "data=" + data +
                ", pageable=" + pageable +
                '}';
    }

    // 빌더 패턴 적용
    public static <T> RequestListBuilder<T> builder() {
        return new RequestListBuilder<>();
    }

    public static class RequestListBuilder<T> {
        private T data;
        private Pageable pageable;

        public RequestListBuilder() {
        }

        public RequestListBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public RequestListBuilder<T> pageable(Pageable pageable) {
            this.pageable = pageable;
            return this;
        }

        public RequestList<T> build() {
            return new RequestList<>(this.data, this.pageable);
        }

        @Override
        public String toString() {
            return "RequestListBuilder{" +
                    "data=" + data +
                    ", pageable=" + pageable +
                    '}';
        }

    }
}

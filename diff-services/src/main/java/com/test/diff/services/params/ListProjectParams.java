package com.test.diff.services.params;

import lombok.*;

/**
 * @author wl
 */
@Getter
@Setter
public class ListProjectParams extends BaseParams{

    private int projectId;

    public static Builder builder(){
        return new Builder();
    };

    public static class Builder{

        private int page;
        private int size;
        private int projectId;

        public Builder page(int page){
            this.page = page;
            return this;
        }

        public Builder size(int size){
            this.size = size;
            return this;
        }

        public Builder projectId(int projectId){
            this.projectId = projectId;
            return this;
        }

        public ListProjectParams build(){
            ListProjectParams params = new ListProjectParams();
            params.setPage(this.page);
            params.setSize(this.size);
            params.setProjectId(this.projectId);
            return params;
        }

    }

}

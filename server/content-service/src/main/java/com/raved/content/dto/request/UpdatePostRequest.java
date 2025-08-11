package com.raved.content.dto.request;

import lombok.Data;

/**
 * UpdatePostRequest for TheRavedApp
 */
@Data
public class UpdatePostRequest {

    private String content;

    private String visibility;

    private Boolean allowComments;

    private Boolean allowSharing;

    // Getters and setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }

    public Boolean getAllowComments() { return allowComments; }
    public void setAllowComments(Boolean allowComments) { this.allowComments = allowComments; }

    public Boolean getAllowSharing() { return allowSharing; }
    public void setAllowSharing(Boolean allowSharing) { this.allowSharing = allowSharing; }
}

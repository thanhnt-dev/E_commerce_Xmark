package com.thanhnt.messageservice.api.request;

import lombok.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMessageCriteria {
  private Integer currentPage;
  private Integer pageSize;

  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}

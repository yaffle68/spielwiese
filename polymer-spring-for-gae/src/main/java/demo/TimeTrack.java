package demo;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by simon.harding on 15.02.2017.
 */
public class TimeTrack {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date m_from;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date m_to;

    public Date getFrom() {
        return m_from;
    }

    public void setFrom(Date from) {
        this.m_from = from;
    }

    public Date getTo() {
        return m_to;
    }

    public void setTo(Date to) {
        this.m_to = to;
    }
}

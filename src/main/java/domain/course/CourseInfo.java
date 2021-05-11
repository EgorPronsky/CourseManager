package domain.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;

@Embeddable

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfo {

    private String name;

    @Type(type="text")
    private String description;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    @Column(name = "time_table")
    @Type(type="text")
    private String timeTable;

    @Type(type="text")
    private String uri;
}

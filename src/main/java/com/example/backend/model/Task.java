package com.example.backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "is_Done", nullable = false, columnDefinition = "boolean default false")
    private boolean isDone;

    @Column(name = "position", nullable = false)
    private Integer position;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)                
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

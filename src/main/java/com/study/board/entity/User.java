package com.study.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

// User Entity
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private int height;

    @Column(nullable = false)
    private int weight;

    @Column(nullable = false)
    private int benchMax;

    @Column(nullable = false)
    private int deadliftMax;

    @Column(nullable = false)
    private int squatMax;

    private double score;

    private String rank;

    public void encodePassword(BCryptPasswordEncoder encoder) {
        this.password = encoder.encode(this.password);
    }

    // ✅ 한 사용자가 여러 개의 게시글 작성 가능
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    public void calculateScoreAndRank() {
        this.score = (benchMax * 1) + (deadliftMax * 1.5) + (squatMax * 1.2);

        if (score < 100) {
            this.rank = "빨";  // 0 ~ 99
        } else if (score < 200) {
            this.rank = "주";  // 100 ~ 199
        } else if (score < 300) {
            this.rank = "노";  // 200 ~ 299
        } else if (score < 400) {
            this.rank = "초";  // 300 ~ 399
        } else if (score < 500) {
            this.rank = "파";  // 400 ~ 499
        } else if (score < 600) {
            this.rank = "남";  // 500 ~ 599
        } else {
            this.rank = "보";  // 600 ~ 699
        }
    }

}

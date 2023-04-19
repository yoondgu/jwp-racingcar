package racingcar.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import racingcar.repository.dao.JdbcPlayRecordsDao;

@SpringBootTest
class JdbcPlayRecordsDaoTest {

    @Autowired
    private JdbcPlayRecordsDao playRecordsDao;

    @DisplayName("DB: 게임 이력 저장 테스트")
    @Test
    void insert2() {
        playRecordsDao.insert(2);

        assertThat(playRecordsDao.getLastId()).isEqualTo(1);
    }
}
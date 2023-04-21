package racingcar.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CarTest {

    private static final long PLAY_RECORD_ID = 1L;

    @Nested
    @DisplayName("자동차 생성 기능 테스트")
    class constructorTest {

        @ParameterizedTest
        @ValueSource(strings = {"자동차", "1", "일이삼사오"})
        void 자동차_생성_성공_테스트(final String name) {
            final Car car = Car.ofPositionStart(PLAY_RECORD_ID, name);

            assertThat(car.getName()).isEqualTo(name);
            assertThat(car.getPosition()).isEqualTo(0);
        }

        @ParameterizedTest
        @ValueSource(strings = {"123456", "일이삼사오육"})
        void 자동차_생성_이름길이_예외_테스트(final String name) {
            assertThatThrownBy(() -> Car.ofPositionStart(PLAY_RECORD_ID, name))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 자동차_생성_이름_공백또는Null_예외_테스트(final String name) {
            assertThatThrownBy(() -> Car.ofPositionStart(PLAY_RECORD_ID, name))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("자동차 생성 외 기능 테스트")
    class moveOrStayByPowerTest {

        private Car car;

        @BeforeEach
        void initializeCar() {
            car = Car.ofPositionStart(PLAY_RECORD_ID, "자동차");
        }

        @ParameterizedTest
        @CsvSource(value = {"-1:false", "0:false", "3:false", "4:true", "5:true", "9:true"}, delimiter = ':')
        void 자동차_전진_또는_유지_테스트(final int power, final boolean expected) {
            assertThat(car.moveOrStayByPower(power)).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, Integer.MAX_VALUE})
        void 자동차_전진_후_위치증가_테스트(final int count) {
            for (int i = 0; i < count; i++) {
                car.moveOrStayByPower(4);
            }

            assertThat(car.getPosition()).isEqualTo(count);
        }

        @Test
        void 자동차_위치_비교_테스트() {
            Car other = Car.of(PLAY_RECORD_ID, "자동차2", Integer.MAX_VALUE);

            assertThat(car.compareTo(other)).isLessThan(0);
        }
    }
}

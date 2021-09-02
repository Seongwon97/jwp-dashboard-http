package nextstep.jwp.framework.infrastructure.http.content;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ContentType 단위 테스트")
class ContentTypeTest {

    @DisplayName("find 메서드는")
    @Nested
    class Describe_find {

        @DisplayName("파일 경로가 주어지면")
        @Nested
        class Context_file_path {

            @DisplayName("html, css, js 확장자를 가려낼 수 있다.")
            @Test
            void it_finds_file_extension() {
                // given, when
                ContentType contentType = ContentType.find("/static/app/index.js");

                // then
                assertThat(contentType).isEqualTo(ContentType.JS);
            }
        }

        @DisplayName("CSS, JS, HTML 형식이 아닌 경우")
        @Nested
        class Context_file_path_undefined {

            @DisplayName("Text/Plain이 반환된다.")
            @Test
            void it_returns_text_plain() {
                // given, when
                ContentType contentType = ContentType.find("/static/app/abc.favicon");

                // then
                assertThat(contentType).isEqualTo(ContentType.PLAIN);
            }
        }
    }
}

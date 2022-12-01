package pl.sterniczuk.passwordWallet.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class AesSencTest {

    @ParameterizedTest
    @MethodSource("encryptPositive")
    public void encrypt_positive_result(String data, String pass, String expectedResult) throws Exception {
        //when
        String results = AesSenc.encrypt(data, pass);
        //then
        Assertions.assertEquals(results, expectedResult);
    }

    @ParameterizedTest
    @MethodSource("encryptPositive")
    public void decrypt_positive_results(String data, String pass, String encryptedTest) throws Exception {
        //when
        String results = AesSenc.decrypt(encryptedTest, pass);

        //then
        Assertions.assertEquals(results, data);
    }

    private Stream<Arguments> encryptPositive() {
        return Stream.of(Arguments.of("Miała baba koguta", "hasło", "54FrDkUS1iGcOCtw6bMAvj2O19ksA44S5T6mb7AA720="),
                Arguments.of("Jakieś przykładowe dane", "hasło", "XkcjIOknQZqb3ZOMbi1FCnx/+OaDGAddjjXqzxafffs="));
    }
}

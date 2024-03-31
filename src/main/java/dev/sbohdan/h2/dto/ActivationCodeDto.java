package dev.sbohdan.h2.dto;

public final class ActivationCodeDto {
    private String activationCode;

    public ActivationCodeDto() {
    }

    public ActivationCodeDto(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}

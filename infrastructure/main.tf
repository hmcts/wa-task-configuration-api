provider "azurerm" {
  features {}
}

locals {
  S2S_SECRET_WORKFLOW_API = "${data.azurerm_key_vault_secret.s2s_secret.value}"
  IA_IDAM_REDIRECT_URI = "${data.azurerm_key_vault_secret.idam_redirect_uri.value}"
  TEST_LAW_FIRM_A_USERNAME = "${data.azurerm_key_vault_secret.idam_username.value}"
  TEST_LAW_FIRM_A_PASSWORD = "${data.azurerm_key_vault_secret.idam_password.value}"

    ia_preview_vault_name     = "ia-aat"
    ia_non_preview_vault_name = "ia-${var.env}"
    ia_key_vault_name         = "${var.env == "preview" || var.env == "spreview" ? local.ia_preview_vault_name : local.ia_non_preview_vault_name}"

}

data "azurerm_key_vault" "wa_key_vault" {
  name                = "${var.product}-${var.env}"
  resource_group_name = "${var.product}-aat"
}

data "azurerm_key_vault" "s2s_key_vault" {
  name                = "s2s-${var.env}"
  resource_group_name = "rpe-service-auth-provider-${var.env}"
}

data "azurerm_key_vault" "ia_key_vault" {
  name                = "ia-${var.env}"
  resource_group_name = "ia-aat"
}

data "azurerm_key_vault_secret" "s2s_secret" {
  key_vault_id = data.azurerm_key_vault.s2s_key_vault.id
  name         = "microservicekey-wa-workflow-api"
}

data "azurerm_key_vault_secret" "idam-redirect-uri" {
  key_vault_id = data.azurerm_key_vault.ia_key_vault.id
  name         = "idam-redirect-uri"
}

data "azurerm_key_vault_secret" "idam_username" {
  key_vault_id = data.azurerm_key_vault.ia_key_vault.id
  name         = "test-law-firm-a-username"
}

data "azurerm_key_vault_secret" "idam_password" {
  key_vault_id = data.azurerm_key_vault.ia_key_vault.id
  name         = "test-law-firm-a-password"
}

resource "azurerm_key_vault_secret" "wa_workflow_s2s_secret" {
  name = "wa-workflow-s2s-secret"
  value = data.azurerm_key_vault_secret.s2s_secret.value
  key_vault_id = data.azurerm_key_vault.wa_key_vault.id
}


resource "azurerm_key_vault_secret" "wa_idam_username" {
  name = "wa-idam-username"
  value = data.azurerm_key_vault_secret.idam_username.value
  key_vault_id = data.azurerm_key_vault.wa_key_vault.id
}

resource "azurerm_key_vault_secret" "wa_idam_password" {
  name = "wa-idam-password"
  value = data.azurerm_key_vault_secret.idam_password.value
  key_vault_id = data.azurerm_key_vault.wa_key_vault.id
}

resource "azurerm_key_vault_secret" "wa_idam_redirect_url" {
  name = "wa-idam-redirect-uri"
  value = data.azurerm_key_vault_secret.idam-redirect-uri.value
  key_vault_id = data.azurerm_key_vault.wa_key_vault.id
}



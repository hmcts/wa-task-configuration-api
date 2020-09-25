provider "azurerm" {
  features {}
}

locals {

  local_env = (var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env

  // Vault name
  previewVaultName = "${var.raw_product}-aat"
  nonPreviewVaultName = "${var.raw_product}-${var.env}"
  vaultName = (var.env == "preview" || var.env == "spreview") ? local.previewVaultName : local.nonPreviewVaultName

  // Shared Resource Group
  previewResourceGroup = "${var.raw_product}-shared-infrastructure-aat"
  nonPreviewResourceGroup = "${var.raw_product}-shared-infrastructure-${var.env}"
  sharedResourceGroup = (var.env == "preview" || var.env == "spreview") ? local.previewResourceGroup : local.nonPreviewResourceGroup

}

data "azurerm_key_vault" "wa_key_vault" {
  name = local.vaultName
  resource_group_name = local.sharedResourceGroup
}

data "azurerm_key_vault" "s2s_key_vault" {
  name                = "s2s-${local.local_env}"
  resource_group_name = "rpe-service-auth-provider-${local.local_env}"
}

data "azurerm_key_vault" "ia_key_vault" {
  name                = "ia-${local.local_env}"
  resource_group_name = "ia-${local.local_env}"
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



variable "product" {}

variable "component" {}

variable "location" {
  default = "UK South"
}
variable "raw_product" {
  default = "wa" // jenkins-library overrides product for PRs and adds e.g. pr-123-ia
}

variable "env" {}

variable "subscription" {}

variable "deployment_namespace" {}

variable "common_tags" {
  type = "map"
}

variable "appinsights_instrumentation_key" {
  default = ""
}

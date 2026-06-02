# Variables pour le projet MentorPath

variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t2.medium"
}

variable "ami_id" {
  description = "Ubuntu 22.04 AMI ID"
  type        = string
  default     = "ami-0c7217cdde317cfec" # Ubuntu 22.04 us-east-1
}

variable "key_name" {
  description = "AWS Key Pair name"
  type        = string
  default     = "mentorpath-key"
}

variable "project_name" {
  description = "Project name"
  type        = string
  default     = "mentorpath"
}
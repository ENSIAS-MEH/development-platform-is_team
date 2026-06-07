# Provider AWS
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# Security Group — ouvre les ports nécessaires
resource "aws_security_group" "mentorpath_sg" {
  name        = "${var.project_name}-sg"
  description = "Security group for MentorPath application"

  # SSH
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "SSH"
  }

  # Backend Spring Boot
  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "mentorpath-backend"
  }

  # Frontend React
  ingress {
    from_port   = 4200
    to_port     = 4200
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "mentorpath-frontend"
  }

  # HTTP
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "HTTP"
  }

  # Tout le trafic sortant autorisé
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name    = "${var.project_name}-sg"
    Project = var.project_name
  }
}

# EC2 Instance
resource "aws_instance" "mentorpath_server" {
  ami                    = var.ami_id
  instance_type          = var.instance_type
  key_name               = var.key_name
  vpc_security_group_ids = [aws_security_group.mentorpath_sg.id]

  # Script d'installation automatique au démarrage
  user_data = <<-EOF
    #!/bin/bash
    apt-get update -y
    apt-get install -y docker.io git
    systemctl start docker
    systemctl enable docker
    usermod -aG docker ubuntu
    curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
    cd /home/ubuntu
    git clone -b develop https://github.com/ENSIAS-MEH/development-platform-is_team.git
    cd development-platform-is_team
    docker compose up -d
  EOF

  tags = {
    Name    = "${var.project_name}-server"
    Project = var.project_name
  }
}
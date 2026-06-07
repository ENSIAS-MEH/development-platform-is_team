# Outputs — affiche les informations après création

output "instance_public_ip" {
  description = "IP publique de l'instance EC2"
  value       = aws_instance.mentorpath_server.public_ip
}

output "instance_public_dns" {
  description = "DNS public de l'instance EC2"
  value       = aws_instance.mentorpath_server.public_dns
}

output "frontend_url" {
  description = "URL du frontend"
  value       = "http://${aws_instance.mentorpath_server.public_ip}:4200"
}

output "backend_url" {
  description = "URL du backend"
  value       = "http://${aws_instance.mentorpath_server.public_ip}:8080"
}

output "swagger_url" {
  description = "URL de Swagger UI"
  value       = "http://${aws_instance.mentorpath_server.public_ip}:8080/swagger-ui.html"
}

output "ssh_command" {
  description = "Commande SSH pour se connecter"
  value       = "ssh -i ${var.key_name}.pem ubuntu@${aws_instance.mentorpath_server.public_ip}"
}
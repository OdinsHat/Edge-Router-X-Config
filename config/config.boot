firewall {
    all-ping enable
    broadcast-ping disable
    ipv6-receive-redirects disable
    ipv6-src-route disable
    ip-src-route disable
    log-martians enable
    name WAN_IN {
        default-action drop
        description "WAN to internal"
        rule 10 {
            action accept
            description "Allow established/related"
            state {
                established enable
                related enable
            }
        }
        rule 20 {
            action drop
            description "Drop invalid state"
            state {
                invalid enable
            }
        }
    }
    name WAN_LOCAL {
        default-action drop
        description "WAN to router"
        rule 10 {
            action accept
            description "Allow established/related"
            state {
                established enable
                related enable
            }
        }
        rule 20 {
            action drop
            description "Drop invalid state"
            state {
                invalid enable
            }
        }
    }
    receive-redirects disable
    send-redirects enable
    source-validation disable
    syn-cookies enable
}
interfaces {
    ethernet eth0 {
        address dhcp
        description Internet
        dhcp-options {
            default-route update
            default-route-distance 210
            name-server no-update
        }
        duplex auto
        firewall {
            in {
                name WAN_IN
            }
            local {
                name WAN_LOCAL
            }
        }
        speed auto
    }
    ethernet eth1 {
        address 192.168.1.1/24
        description Local
        dhcp-options {
            default-route update
            default-route-distance 210
            name-server update
        }
        duplex auto
        speed auto
    }
    ethernet eth2 {
        address 192.168.2.1/24
        description "Local 2"
        duplex auto
        speed auto
    }
    ethernet eth3 {
        duplex auto
        speed auto
    }
    loopback lo {
    }
}
service {
    dhcp-server {
        disabled false
        hostfile-update disable
        shared-network-name LAN1 {
            authoritative enable
            subnet 192.168.1.0/24 {
                default-router 192.168.1.1
                dns-server 208.67.222.222
                dns-server 192.168.1.1
                lease 86400
                start 192.168.1.38 {
                    stop 192.168.1.243
                }
            }
        }
        shared-network-name LAN2 {
            authoritative enable
            subnet 192.168.2.0/24 {
                default-router 192.168.2.1
                dns-server 192.168.2.1
                lease 86400
                start 192.168.2.38 {
                    stop 192.168.2.243
                }
            }
        }
        use-dnsmasq disable
    }
    dns {
        forwarding {
            cache-size 150
            listen-on eth1
            listen-on eth2
            name-server 8.8.8.8
            name-server 1.1.1.1
            name-server 8.8.4.4
            name-server 208.67.222.222
            name-server 208.67.220.220
        }
    }
    gui {
        http-port 80
        https-port 443
        older-ciphers enable
    }
    nat {
        rule 5010 {
            description "masquerade for WAN"
            outbound-interface eth0
            type masquerade
        }
    }
    snmp {
        community ro {
            authorization ro
        }
        contact "Doug Bromley <doug@tintophat.com>"
        location "Birmingham, UK"
    }
    ssh {
        port 22
        protocol-version v2
    }
    unms {
        disable
    }
}
system {
    domain-name tintophat.com
    host-name home
    login {
        user Osiris {
            authentication {
                encrypted-password $6$9PqXBgiXTZXMMk5$890OLSIntoqqglB50e3RBMVv4.iW9sT43OPHe8P1VQhgAznd30eRNZW9p0xLVd2LLXPl7XHkbSKxcwJ8CAe5O/
            }
            level admin
        }
    }
    name-server 127.0.0.1
    ntp {
        server 0.ubnt.pool.ntp.org {
        }
        server 1.ubnt.pool.ntp.org {
        }
        server 2.ubnt.pool.ntp.org {
        }
        server 3.ubnt.pool.ntp.org {
        }
    }
    syslog {
        global {
            facility all {
                level notice
            }
            facility protocols {
                level debug
            }
        }
    }
    time-zone UTC
    traffic-analysis {
        dpi enable
        export enable
    }
}


/* Warning: Do not remove the following line. */
/* === vyatta-config-version: "config-management@1:conntrack@1:cron@1:dhcp-relay@1:dhcp-server@4:firewall@5:ipsec@5:nat@3:qos@1:quagga@2:system@4:ubnt-pptp@1:ubnt-unms@1:ubnt-util@1:vrrp@1:webgui@1:webproxy@1:zone-policy@1" === */
/* Release version: v1.9.8.5012183.170825.0258 */

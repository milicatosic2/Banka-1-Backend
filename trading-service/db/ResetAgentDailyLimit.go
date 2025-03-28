package db

import (
	"fmt"

	"banka1.com/types"
	"github.com/robfig/cron/v3"
)

// cron posao koji resetuje limit agentu svakog dana u 23 59
func StartScheduler() {
	c := cron.New()
	_, err := c.AddFunc("59 23 * * *", func() {
		resetDailyLimits()
	})

	if err != nil {
		fmt.Println("Greska pri pokretanju cron job-a:", err)
		return
	}

	c.Start()
}

func resetDailyLimits() {
	DB.Model(&types.Actuary{}).Where("role = ?", "agent").Update("usedLimit", 0)
}

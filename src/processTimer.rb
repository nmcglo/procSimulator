
class ProjectTimings < Object
	def initialize
		@timingMap ={}
		@list
		@wt = {rmWt:0, remBurstTime:0}
	end
	def createDefault()
		@timingMap = {idle: 0, userWait:0, IOWait:0, active: 0,
    terminated: 0, contextSwitch:0, "idle"=>0}
	end
	def createCustom(strList)
		strList.each {|st| @timingMap[st] = 0}
	end
	def createCustomInt
		$tList.each {|st| @timingMap[st] = 0}
	end	
	def tickTime(tpi)
		@timingMap[tpi] -= 1
		if(tpi == "IOWait" or tpi == "userWait")
			@wt[:rmWt] -= 1
		end
		@wt[:remBurstTime] if tpi == "active"

		@wt[:rmwt] = 0 unless @wt[:rmwt] >= 0
		@wt[:remBurstTime] = 0 unless @wt[:remBurstTime] >= 0
	end
	def getTiming(tpi)
		return @timingMap[tpi]
	end
	def isWaitDone
		return (@wt[:rmWt] == 0)
	end
	def isBurstDone
		return (@wt[:remBurstTime] == 0)
	end
	def setNewBurst
		@wt[:remBurstTime] = Random.rand(20...200)
	end
	def setNewIO
		@wt[:rmwt] = Random.rand(1200...3200)
	end
	def setNewIntTime
		@wt[:rmwt] = Random.rand(1000...4500)
	end
	def getCWait
		return @wt[:rmwt]
	end
	def getCBurst
		return @wt[:remBurstTime]
	end
	def getIntTime
	return Random.rand(1000...4500)
	end
	def getIOTime
		return Random.rand(1200...3200)
	end
	def getBurstTime
		return Random.rand(20...200)
	end


		
end




ProjectTimings.new()


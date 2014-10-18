
class ProjectTimings 
	def initialize()
		@timingMap ={}
		@list
	end
	def createDefault()
		@timingMap = {idle: 0, userWait:0, IOWait:0, active: 0,
    terminated: 0, contextSwitch:0}
	end
	def createCustom(strList)
		strList.each {|st| @timingMap[st] = 0}
	end
	def createCustomInt
		$tList.each {|st| @timingMap[st] = 0}
	end	
	def tickTime(tpi)
		@timingMap[tpi] += 1
	end
	def getTiming(tpi)
		@timingMap[tpi]
	end
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



ProjectTimings.new()


